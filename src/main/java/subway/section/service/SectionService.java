package subway.section.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.service.LineService;
import subway.section.controller.dto.SectionRequest;
import subway.section.domain.Section;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;
import subway.station.service.StationService;

@Service
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;

    public SectionService(LineService lineService,
            StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.getLine(lineId);
        List<Long> stationId = Arrays.asList(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());
        List<Station> stations = stationService.findStationsByIdList(stationId);
        SectionStations sectionStations = SectionStations.createSectionStations(stations);
        Section section = new Section(line, sectionStations, sectionRequest.getDistance());
        line.addSection(section);
    }
}
