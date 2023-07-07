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
        // TODO : 여기 컴포짓 뜯어 고쳐야됨.
        return lineService.saveLine(lineRequest, upStation, downStation);
    }
}
