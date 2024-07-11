package subway;

import org.springframework.stereotype.Component;

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
        return sectionService.createSection(dto);
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
        String lineName = lineService.readLine(firstSection.getLineId()).getName();
        SectionResponse sectionResponse = new SectionResponse(firstSection.getLineId(), lineName);

        Map<Long, String> stationMap = new HashMap<>();
        for(Section section: sections) {
            StationResponse upStationResponse = stationService.findStation(section.getUpStationId());
            StationResponse downStationResponse = stationService.findStation(section.getDownStationId());
            stationMap.put(upStationResponse.getId(), upStationResponse.getName());
            stationMap.put(downStationResponse.getId(), downStationResponse.getName());
        }
        for (Map.Entry<Long, String> entry : stationMap.entrySet()) {
            StationResponse stationResponse = new StationResponse(entry.getKey(), entry.getValue());
            sectionResponse.addUpStation(stationResponse);
        }

        return sectionResponse;
    }
}
