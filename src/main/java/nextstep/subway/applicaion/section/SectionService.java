package nextstep.subway.applicaion.section;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionCreationRequest;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public Section createInitialSection(Long lineId, SectionCreationRequest request) {
        var upStation = getStation(request.getUpStationId());
        var downStation = getStation(request.getDownStationId());
        var firstSection = sectionRepository.save(new Section(lineId, upStation));
        var secondSection = sectionRepository.save(new Section(lineId, downStation));
        firstSection.setNextSection(secondSection, request.getDistance());
        secondSection.setPrevSection(firstSection);
        return firstSection;
    }

    public void addSection(Long lineId, SectionCreationRequest request) {
        var upStation = getStation(request.getUpStationId());
        var downStation = getStation(request.getDownStationId());
        var section = sectionRepository.findByLineIdAndStation(lineId, upStation)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선이거나 노선에 상행역이 없습니다."));

        if (section.getNextSection() != null) {
            throw new IllegalStateException("종점이 아닌 역에 구간을 추가할 수 없습니다.");
        }

        if (isExistSection(lineId, downStation)) {
            throw new IllegalStateException("이미 노선에 존재하는 역입니다.");
        }

        var nextSection = sectionRepository.save(new Section(lineId, downStation));
        section.setNextSection(nextSection, request.getDistance());
        nextSection.setPrevSection(section);
    }

    public void deleteSection(Long lineId, Long stationId) {
        var station = getStation(stationId);
        sectionRepository.findByLineIdAndStation(lineId, station)
                .ifPresent(section -> {
                    section.getPrevSection().resetNextSection();
                    sectionRepository.delete(section);
                });
    }

    public void removeSections(Long lineId) {
        sectionRepository.deleteAllByLineId(lineId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }

    private boolean isExistSection(Long lineId, Station station) {
        return sectionRepository.findByLineIdAndStation(lineId, station).isPresent();
    }
}
