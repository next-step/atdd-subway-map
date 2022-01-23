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
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map( line ->
                        new LineResponse(
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
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.getById(id);
        line.modify(lineRequest.getName(), lineRequest.getColor());
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.getById(id);
        lineRepository.deleteById(line.getId());
    }
}
