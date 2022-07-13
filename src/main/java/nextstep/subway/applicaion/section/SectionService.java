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
        var firstSection = sectionRepository.save(new Section(lineId, upStation, request.getDistance()));
        var secondSection = sectionRepository.save(new Section(lineId, downStation));
        firstSection.setNextSection(secondSection);
        return firstSection;
    }

    public void removeSections(Long lineId) {
        sectionRepository.deleteAllByLineId(lineId);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }
}
