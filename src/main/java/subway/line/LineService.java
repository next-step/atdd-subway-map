package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;

    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        final var line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        lineRequest.getUpStationId(),
                        lineRequest.getDownStationId(),
                        lineRequest.getDistance()));

        return createLineResponse(line);
    }

    public LineResponse findLineById(Long id) {
        final var line = findById(id);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        final var line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        final var line = findById(id);
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationService.findStationsByIds(
                        List.of(line.getUpStationId(), line.getDownStationId())));
    }
}
