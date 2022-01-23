package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.LineNameDuplicationException;
import nextstep.subway.exception.NotFoundLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new LineNameDuplicationException(request.getName());
        }
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }


    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.from(line);
    }

    public void changeLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));

        line.update(lineRequest.getColor(), lineRequest.getName());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
