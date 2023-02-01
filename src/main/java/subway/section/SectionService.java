package subway.section;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;

@Service
public class SectionService {

    private final StationService stationService;
    private final SectionRepository sectionRepository;
    public SectionService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public Section createSection(SectionRequest sectionRequest) {
        Station upStation = this.stationService.findOne(sectionRequest.getUpStationId());
        Station downStation = this.stationService.findOne(sectionRequest.getDownStationId());

        Section section = new Section(sectionRequest.getDistance(), upStation, downStation);
        return this.sectionRepository.save(section);
    }

    public Boolean isAppendable(Line line, SectionRequest sectionRequest) {
        List<Section> sections = line.getSections();
        Long downStationId = line.getDownStation().getId();

        Long requestUpStationId = sectionRequest.getUpStationId();
        Long requestDownStationId = sectionRequest.getDownStationId();

        if(!downStationId.equals(requestUpStationId))
            return Boolean.FALSE;

        if(hasAppended(sections, requestDownStationId))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    public Boolean isDeletable(Line line, Long stationId) {
        if(!hasDeletableDownStation(line, stationId))
            return Boolean.FALSE;

        if(hasSingleSection(line))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    private Boolean hasAppended(List<Section> sections, Long downStationId) {
        return sections.stream()
                .anyMatch((s) -> s.getUpStation().getId().equals(downStationId) ||
                        s.getDownStation().getId().equals(downStationId));
    }

    private Boolean hasDeletableDownStation(Line line, Long downStationId) {
        return downStationId.equals(line.getDownStation().getId());
    }

    private Boolean hasSingleSection(Line line) {
        final int CANNOT_DELETE_SECTION_SIZE = 1;
        return line.getSections().size() == CANNOT_DELETE_SECTION_SIZE;
    }
}
