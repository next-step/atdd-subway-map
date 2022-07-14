package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        final Line line = lineRepository.save(lineRequest.toEntity());
        return line.toResponse();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(Line::toResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        return lineRepository.findById(id).orElseThrow().toResponse();
    }

    @Transactional
    public void updateLineById(
            final Long id,
            final LineRequest request
    ) {
        lineRepository.save(request.toEntity(id));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

}
