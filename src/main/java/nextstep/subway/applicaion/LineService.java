package nextstep.subway.applicaion;

import nextstep.subway.applicaion.converter.ResponseConverter;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.LineNotFoundException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return ResponseConverter.toLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(ResponseConverter::toLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long lineId) throws LineNotFoundException {
        return lineRepository.findById(lineId)
                .map(ResponseConverter::toLineResponse)
                .orElseThrow(() -> new LineNotFoundException("INVALID LINE id: " + lineId));
    }

    public boolean updateLine(Long lineId, LineRequest request) {
        Optional<Line> lineOptional = lineRepository.findById(lineId);
        if (lineOptional.isPresent()) {
            Line line = lineOptional.get();
            line.change(request.getName(), request.getColor());
            return true;
        }

        lineRepository.save(new Line(lineId, request.getName(), request.getColor()));
        return false;
    }
}
