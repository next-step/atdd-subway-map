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
        List<Station> stations = getStationList(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());
        Section section = createSection(line, stations, sectionRequest.getDistance());
        line.addSection(section);
    }

    private List<Station> getStationList(Long upwardId, Long downwardId) {
        List<Long> stationId = Arrays.asList(upwardId, downwardId);
        return stationService.findStationsByIdList(stationId);
    }

    private Section createSection(Line line, List<Station> stations, Integer distance) {
        SectionStations sectionStations = SectionStations.createSectionStations(stations);
        return new Section(line, sectionStations, distance);
    }
}
