package nextstep.subway.applicaion.section;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public Section createInitialSection(Long upStationId, Long downStationId, Long distance) {
        var upStation = getStation(upStationId);
        var downStation = getStation(downStationId);
        var firstSection = sectionRepository.save(new Section(upStation, distance));
        var secondSection = sectionRepository.save(new Section(downStation));
        firstSection.setNextSection(secondSection);

        return firstSection;
    }

    public void removeSections(Section firstSection) {
        var sections = new ArrayList<Section>();
        var currentSection = firstSection;
        while (currentSection != null) {
            sections.add(currentSection);
            currentSection = currentSection.getNextSection();
        }
        sectionRepository.deleteAll(sections);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }
}
