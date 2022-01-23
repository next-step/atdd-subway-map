package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
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

    @Transactional
    public LineResponse saveLine(LineCreateRequest request) {
        boolean isExists = lineRepository.existsByName(request.getName());
        if (isExists) {
            throw new IllegalArgumentException();
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> searchAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse searchLine(final Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);

        return new LineResponse(line);
    }

    @Transactional
    public void updateLine(final LineUpdateRequest request) {
        boolean isExists = lineRepository.existsByName(request.getName());
        Line line = lineRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);

        if (isExists && !line.getName().equals(request.getName())) {
            throw new IllegalArgumentException();
        }

        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
