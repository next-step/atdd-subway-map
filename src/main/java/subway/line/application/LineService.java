package subway.line.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.station.application.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
            new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            List.of(stationService.findStation(line.getUpStationId()), stationService.findStation(line.getDownStationId()))
        );
    }

}
