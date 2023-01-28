package subway.route;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.StationResponse;
import subway.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line route = new Line(lineRequest.getName(), lineRequest.getColor(),
                lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        Line newLine = lineRepository.save(route);
        List<StationResponse> stationResponses = stationService.findAllById(List.of(lineRequest.getUpStationId(), lineRequest.getDownStationId()));
        return new LineResponse(newLine.getId(), newLine.getName(), newLine.getColor(),stationResponses);
    }
}
