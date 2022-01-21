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

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> new LineResponse(
                        line.getId(),
                        line.getName(),
                        line.getColor(),
                        line.getCreatedDate(),
                        line.getModifiedDate()
                )).collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        return lineRepository.findById(id).map(line -> new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        )).orElseThrow(()-> new IllegalArgumentException("invalid id"));
    }
}
