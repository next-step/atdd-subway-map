package subway.linesection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;

@Service
public class LineSectionService {
    private final LineService lineService;
    private final StationService stationService;

    public LineSectionService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }


    @Transactional
    public void addSection(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        Line line = lineService.getLine(lineId);
        Station upStation = stationService.getStation(upStationId);
        Station downStation = stationService.getStation(downStationId);
        line.addSection(LineSection.of(line, upStation, downStation, distance));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.getLine(lineId);
        Station toDeleteStation = stationService.getStation(stationId);
        line.removeSection(toDeleteStation);
    }
}
