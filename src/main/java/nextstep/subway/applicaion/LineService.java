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
    private final LineRepository lineRepository;

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

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }
<<<<<<< HEAD

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 노선"));
        return LineResponse.of(line);
    }

    public void update(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 노선"));
        line.update(request);
    }
<<<<<<< HEAD

    public void delete(Long id) {
        lineRepository.deleteById(id);
    }
=======
>>>>>>> c2a4b18 (feat : 지하철 노선 목록 조회)
=======
>>>>>>> af7461e (feat : 노선 업데이트)
}
