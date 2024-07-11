package subway.section;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.station.StationResponse;
import subway.station.StationService;
import subway.line.LineService;

import java.util.*;

@Component
public class SectionFacade {

    private final LineService lineService;

    private final StationService stationService;

    private final SectionService sectionService;

    public SectionFacade(LineService lineService, StationService stationService, SectionService sectionService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public Long createSection(SectionCreateDTO dto) {
        lineService.validateSectionCreate(dto);
        stationService.validateSectionCreate(dto);
        Line line = lineService.readLine(dto.getLineId());
        return sectionService.createSection(dto, line);
    }

    public SectionResponse readSections(Long lineId) {
        List<Section> sections = sectionService.readSections(lineId);
        return makeSectionResponse(sections);
    }
    public void deleteSection(SectionDeleteDTO dto) {
        sectionService.deleteSection(dto);
    }

    private SectionResponse makeSectionResponse(List<Section> sections) {
        Section firstSection = sections.get(0);
        String lineName = lineService.readLine(firstSection.getLine().getId()).getName();
        SectionResponse sectionResponse = new SectionResponse(firstSection.getLine().getId(), lineName);


        for(Section section: sections) {
            StationResponse upStationResponse = stationService.findStation(section.getUpStationId());
            StationResponse downStationResponse = stationService.findStation(section.getDownStationId());
            sectionResponse.addUpStation(upStationResponse);
            sectionResponse.addDownStation(downStationResponse);
        }

        return sectionResponse;
    }
}
