package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineReadAllResponse;
import nextstep.subway.applicaion.dto.LineSaveRequest;
import nextstep.subway.applicaion.dto.LineSaveResponse;
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

    public LineSaveResponse saveLine(LineSaveRequest request) {
        final Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineSaveResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineReadAllResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> new LineReadAllResponse(line, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
        // .toUnmodifiableList() 사용하러 했지만, 버전문제인지 컴파일 에러가 떠서 그냥 List 를 사용하고 있습니다.
    }
}
