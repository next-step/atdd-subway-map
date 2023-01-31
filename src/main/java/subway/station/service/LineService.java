package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.LineRepository;
import subway.station.domain.line.Line;
import subway.station.web.dto.LineRequest;
import subway.station.web.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
