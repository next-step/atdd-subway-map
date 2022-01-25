package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.DuplicationNameException;
import nextstep.subway.applicaion.exception.EntityNotFoundException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicationNameException();
        }
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::new)
                .orElseThrow(EntityNotFoundException::new);
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.update(lineRequest.getName(), lineRequest.getColor());
        return new LineResponse(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
