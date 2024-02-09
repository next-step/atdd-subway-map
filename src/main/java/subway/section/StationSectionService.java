package subway.section;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.StationLine;
import subway.line.StationLineRepository;
import subway.line.StationLineRequest;
import subway.line.StationLineResponse;
import subway.line.StationLineService;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;
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

        validateSaveSection(stationLine, stationSection);

        stationLineService.registerStationSection(stationLine, stationSection);
        return stationLine;
    }

    private void validateSaveSection(StationLine stationLine, StationSection stationSection) {
        if (stationLineService.isExistSection(stationLine, stationSection)) {
            throw new IllegalArgumentException("이미 등록된 구간입니다.");
        }

        if (!stationLineService.isConnectedSection(stationLine, stationSection)) {
            throw new IllegalArgumentException("구간이 올바르게 이어지지 않습니다.");
        }
    }

    @Transactional
    public void deleteStationLineById(long lineId, long stationId) {
        StationLine stationLine = stationLineService.findStationLineById(lineId);
        Station station = stationService.findById(stationId);

        if (stationLine.isSingleSection()) {
            throw new IllegalArgumentException("삭제할 구간이 존재하지 않습니다.");
        }

        if (!stationLine.isRemoveFinalSection(station)) {
            throw new IllegalArgumentException("삭제할 구간이 올바르지 않습니다.");
        }

        stationLineService.deleteStationSection(stationLine, station);
    }
}
