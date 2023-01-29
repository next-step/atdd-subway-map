package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.StationService;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.service.dto.LineCreateRequest;
import subway.line.service.dto.LineResponse;
import subway.line.service.dto.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(
        LineRepository lineRepository,
        StationService stationService
    ) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse create(LineCreateRequest request) {
        final Line line = new Line(
            request.getName(),
            request.getColor(),
            request.getUpStationId(),
            request.getDownStationId(),
            request.getDistance()
        );
        final Line savedLine = lineRepository.save(line);
        return createLineResponse(savedLine);
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
            .map(it -> createLineResponse(it))
            .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        final Line line = getLine(id);
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse update(Long id, LineUpdateRequest request) {
        final Line line = getLine(id);
        line.update(request.getName(), request.getColor());

        return createLineResponse(line);
    }

    private Line getLine(Long id) {
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
