package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveStation(LineRequest lineRequest) {
        Line line = lineRepository.save(createLine(lineRequest));
        return createLineResponse(line);
    }

    private Line createLine(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), getStations(line));
    }

    private static List<StationResponse> getStations(Line line) {
        return List.of(getStationResponse(line.getStationLink().getUpStation()), getStationResponse(line.getStationLink().getDownStation()));
    }

    private static StationResponse getStationResponse(Station station) {
        return new StationResponse(station.getId(),
                station.getName());
    }

}
