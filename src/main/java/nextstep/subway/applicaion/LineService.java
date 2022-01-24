package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
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
        checkDuplication(request);
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private void checkDuplication(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("[duplication]:name");
        }
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map( line ->
                        LineResponse.of(
                                line.getId(),
                                line.getName(),
                                line.getColor(),
                                line.getCreatedDate(),
                                line.getModifiedDate()
                        )
                ).collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.getById(id);
        return LineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.getById(id);
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.getById(id);
        lineRepository.deleteById(line.getId());
    }
}
