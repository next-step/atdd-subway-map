package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.LineNameDuplicatedException;
import nextstep.subway.applicaion.exception.LineNotFoundException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        String name = request.getName();
        if (lineRepository.existsByName(name)) {
            throw new LineNameDuplicatedException(name);
        }

        Line line = lineRepository.save(new Line(name, request.getColor()));
        return LineResponse.fromEntity(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long lineId) throws LineNotFoundException {
        return lineRepository.findById(lineId)
                .map(LineResponse::fromEntity)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    public void updateLine(Long lineId, LineRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        line.change(request.getName(), request.getColor());
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
