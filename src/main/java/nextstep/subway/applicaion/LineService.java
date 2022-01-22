package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineCreateRequest;
import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineCreateResponse saveLine(LineCreateRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineCreateResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디를 입력했습니다."));
        return createLineResponse(line);
    }

    public void updateLineById(Long id, LineCreateRequest lineCreateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디를 입력했습니다."));
        Line updateLine =
                new Line(line.getId(), lineCreateRequest.getName(), lineCreateRequest.getColor());

        lineRepository.save(updateLine);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                Collections.EMPTY_LIST,
                line.getCreatedDate(),
                line.getModifiedDate());
    }
}
