package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.StationValidator;
import subway.station.application.StationService;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.application.dto.LineCreateRequest;
import subway.line.application.dto.LineResponse;
import subway.line.application.dto.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final StationValidator stationValidator;

    public LineService(
        LineRepository lineRepository,
        StationService stationService,
        StationValidator stationValidator
    ) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.stationValidator = stationValidator;
    }

    @Transactional
    public LineResponse createLine(LineCreateRequest request) {
        final Line line = new Line(
            request.getName(),
            request.getColor(),
            request.getUpStationId(),
            request.getDownStationId(),
            request.getDistance(),
            stationValidator
        );
        final Line savedLine = lineRepository.save(line);
        return createLineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
            .map(it -> createLineResponse(it))
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        final Line line = findById(id);
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse updateLine(Long id, LineUpdateRequest request) {
        final Line line = findById(id);
        line.update(request.getName(), request.getColor());

        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다."));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stationService.findStationsByIds(
                List.of(
                    line.getUpStationId(),
                    line.getDownStationId()
                )
            )
        );
    }
}
