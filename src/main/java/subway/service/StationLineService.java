package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.request.StationLineRequest;
import subway.controller.resonse.StationLineResponse;
import subway.controller.resonse.StationResponse;
import subway.domain.StationLine;
import subway.repository.StationLineRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository stationLineRepository;
    private final StationService stationService;

    public StationLineService(StationLineRepository stationLineRepository, StationService stationService) {
        this.stationLineRepository = stationLineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        StationLine stationLine = StationLine.create(stationLineRequest.getLineName(), stationLineRequest.getColor(), stationLineRequest.getUpStationId(), stationLineRequest.getDownStationId(), stationLineRequest.getDistance());

        StationLine satedStationLine = stationLineRepository.save(stationLine);
        StationResponse upStation = stationService.findStation(stationLine.getUpStationId());
        StationResponse downStation = stationService.findStation(stationLine.getDownStationId());

        return createStationLineResponse(satedStationLine, upStation, downStation);
    }


    private StationLineResponse createStationLineResponse(StationLine stationLine, StationResponse upStationResponse, StationResponse downStationResponse) {
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                List.of(upStationResponse, downStationResponse)
        );
    }
}
