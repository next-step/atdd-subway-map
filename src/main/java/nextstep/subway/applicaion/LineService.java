package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.LineDuplicateException;
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

        if (lineRepository.findByName(name).isPresent()) {
            throw new LineDuplicateException();
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

    public LineResponse findLineBy(Long id) {
        //TODO: Exception 처리
        Line findLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        return createLineResponse(findLine);
    }

    public void modifyBy(Long id, LineRequest lineRequest) {
        Line findLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        findLine.change(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
