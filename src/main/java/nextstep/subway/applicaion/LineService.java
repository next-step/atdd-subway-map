package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        checkDuplicated(request.getName());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        return LineResponse.of(findById(id));
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.edit(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("지하철 노선을 찾을 수 없습니다."));
    }

    private void checkDuplicated(String name) {
        Optional<Line> findStation = lineRepository.findByName(name);
        if (findStation.isPresent()) {
            throw new RuntimeException("해당 지하철 노선이 이미 존재합니다.");
        }
    }
}
