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
        var firstSection = createSection(lineId, request.getUpStationId());
        var secondSection = createSection(lineId, request.getDownStationId());
        linkSections(firstSection, secondSection, request.getDistance());
        return firstSection;
    }

    public void addSection(Long lineId, SectionCreationRequest request) {
        var upStation = getStation(request.getUpStationId());
        var section = sectionRepository.findByLineIdAndStation(lineId, upStation)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선이거나 노선에 상행역이 없습니다."));
        var nextSection = createSection(lineId, request.getDownStationId());
        linkSections(section, nextSection, request.getDistance());
    }

    public void deleteSection(Long lineId, Long stationId) {
        var station = getStation(stationId);
        var section = sectionRepository.findByLineIdAndStation(lineId, station)
                .orElseThrow(() -> new IllegalStateException("노선에 없는 역을 삭제할 수 없습니다."));

        if (!section.isLastSection()) {
            throw new IllegalStateException("하행 종점이 아니면 구간을 삭제할 수 없습니다.");
        }
        if (section.getPrevSection().isFirstSection()) {
            throw new IllegalStateException("상/하행 종점만 존재하는 경우 구간을 삭제할 수 없습니다.");
        }

        section.getPrevSection().resetNextSection();
        sectionRepository.delete(section);
    }

    public void removeSections(Long lineId) {
        sectionRepository.deleteAllByLineId(lineId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }

    private Section createSection(Long lineId, Long stationId) {
        var station = getStation(stationId);
        if (isExistSection(lineId, station)) {
            throw new IllegalStateException("이미 노선에 존재하는 역입니다.");
        }

        return sectionRepository.save(new Section(lineId, station));
    }

    private boolean isExistSection(Long lineId, Station station) {
        return sectionRepository.findByLineIdAndStation(lineId, station).isPresent();
    }

    private void linkSections(Section upSection, Section downSection, Long distance) {
        if (upSection.getNextSection() != null) {
            throw new IllegalStateException("종점이 아닌 역에 구간을 추가할 수 없습니다.");
        }

        upSection.setNextSection(downSection, distance);
        downSection.setPrevSection(upSection);
    }
}
