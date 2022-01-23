package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineReadAllResponse;
import nextstep.subway.applicaion.dto.LineReadResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineQueryService {
    private LineRepository lineRepository;

    public LineQueryService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineReadAllResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> new LineReadAllResponse(line, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    public LineReadResponse findLine(final Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new LineReadResponse(line, Collections.EMPTY_LIST);
    }
}
