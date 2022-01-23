package nextstep.subway.applicaion;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("아이디에 해당하는 정보가 없습니다."));
        return createLineResponse(line);
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
