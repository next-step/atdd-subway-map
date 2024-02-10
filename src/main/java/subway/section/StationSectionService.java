package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.StationLine;
import subway.line.StationLineService;
import subway.station.Station;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class StationSectionService {
    private StationService stationService;
    private StationLineService stationLineService;

    public StationSectionService(StationService stationService, StationLineService stationLineService) {
        this.stationService = stationService;
        this.stationLineService = stationLineService;
    }

    @Transactional
    public StationLine saveStationSection(long lineId, StationSectionRequest stationSectionRequest) {
        Station upStation = stationService.findById(stationSectionRequest.getUpStationId());
        Station downStation = stationService.findById(stationSectionRequest.getDownStationId());
        StationLine stationLine = stationLineService.findStationLineById(lineId);

        StationSection stationSection = new StationSection(stationLine, upStation, downStation, stationSectionRequest.getDistance());

        stationLine.validateSaveSection(stationSection);

        stationLineService.registerStationSection(stationLine, stationSection);
        return stationLine;
    }

    @Transactional
    public void deleteStationLineById(long lineId, long stationId) {
        StationLine stationLine = stationLineService.findStationLineById(lineId);
        Station station = stationService.findById(stationId);

        stationLine.validateDeleteSection(station);

        stationLineService.deleteStationSection(stationLine, station);
    }
}
