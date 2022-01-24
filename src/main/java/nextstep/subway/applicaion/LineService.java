package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.LineNotFoundException;
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
        validateDuplicateLineName(request.getName());
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private void validateDuplicateLineName(String name) {
        if(lineRepository.findByName(name) != null) {
            throw new RuntimeException("중복된 이름입니다.");
        }
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        return createLineResponse(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        line.update(request.getName(), request.getColor());
        return createLineResponse(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
