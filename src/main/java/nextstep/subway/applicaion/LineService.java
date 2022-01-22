package nextstep.subway.applicaion;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineReadResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineCreateResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineCreateResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    // TODO 서비스 분리 필요할 수 있음(Read, Write 분리)
    @Transactional(readOnly = true)
    public List<LineReadResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineReadResponse.of(line, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    // TODO 서비스 분리 필요할 수 있음(Read, Write 분리)
    @Transactional(readOnly = true)
    public LineReadResponse findSpecificLine(Long id) {
        return lineRepository
                .findById(id)
                .map(line -> LineReadResponse.of(line, Collections.EMPTY_LIST))
                .orElseThrow(NotFoundException::new);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundException::new);

        // TODO question: Entity에까지 LineRequest DTO를 들고 가는게 맞을까요?
        line.changeLineInformation(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
