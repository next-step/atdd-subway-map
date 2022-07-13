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
        return firstSection;
    }

    public void addSection(Long lineId, SectionCreationRequest request) {
        var upStation = getStation(request.getUpStationId());
        var section = sectionRepository.findByLineIdAndStation(lineId, upStation)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선이거나 노선에 상행역이 없습니다."));
        var downStation = getStation(request.getDownStationId());
        var nextSection = sectionRepository.save(new Section(lineId, downStation));
        section.setNextSection(nextSection, request.getDistance());
    }

    public void removeSections(Long lineId) {
        sectionRepository.deleteAllByLineId(lineId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }
}
