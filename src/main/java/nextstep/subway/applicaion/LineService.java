package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
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

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    // TODO 서비스 분리 필요할 수 있음(Read, Write 분리)
    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(
                        line ->
                                // TODO 해당 부분 엔티티/도메인에서 처리할 수 있도록 변경 필요 -> Response 쪽에서 처리하는 것이
                                // 유리해보임
                                new LineResponse(
                                        line.getId(),
                                        line.getName(),
                                        line.getColor(),
                                        line.getCreatedDate(),
                                        line.getModifiedDate()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findSpecificLine(Long id) {
        return lineRepository
                .findById(id)
                .map(
                        line ->
                              // TODO 해당 부분 엔티티/도메인에서 처리할 수 있도록 변경 필요 -> Response 쪽에서 처리하는 것이
                              // 유리해보임
                                new LineResponse(
                                        line.getId(),
                                        line.getName(),
                                        line.getColor(),
                                        line.getCreatedDate(),
                                        line.getModifiedDate()))
                .orElseThrow(NotFoundException::new);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundException::new);

        // TODO question: Entity에까지 LineRequest DTO를 들고 가는게 맞을까요?
        line.changeLineInformation(lineRequest.getName(), lineRequest.getColor());
    }
}
