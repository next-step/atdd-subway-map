package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.station.model.Station;
import subway.station.service.StationService;

@Service
@RequiredArgsConstructor
public class LineStationService {

    private final LineService lineService;
    private final StationService stationService;

    public LineResponse saveLine(LineCreateRequest lineRequest) {
        Station upStation = stationService.findEntityById(lineRequest.getUpStationId());
        Station downStation = stationService.findEntityById(lineRequest.getDownStationId());
        return lineService.saveLine(lineRequest, upStation, downStation);
    }
}
