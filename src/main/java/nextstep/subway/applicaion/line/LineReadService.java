package nextstep.subway.applicaion.line;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineReadResponse;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineReadService {
    private final LineRepository lineRepository;

    public LineReadService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineReadResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineReadResponse.of(line))
                .collect(Collectors.toList());
    }

    public LineReadResponse findSpecificLine(Long id) {
        return lineRepository
                .findById(id)
                .map(line -> LineReadResponse.of(line))
                .orElseThrow(NotFoundException::new);
    }
}
