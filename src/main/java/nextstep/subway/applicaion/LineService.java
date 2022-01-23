package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineDetailResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineDetailResponse getLine(Long id) {
        Optional<Line> optionalLine = lineRepository.findById(id);

        if (optionalLine.isPresent()) {
            return LineDetailResponse.from(optionalLine.get());
        }

        return null;
    }

    public boolean patchLine(Long id, LineRequest lineRequest) {
        Optional<Line> optionalLine = lineRepository.findById(id);

        if (optionalLine.isPresent()) {
            optionalLine.get().modify(lineRequest.getName(), lineRequest.getColor());
            return false;
        }
        return true;
    }

    public boolean deleteLine(Long id) {
        if (lineRepository.existsById(id)) {
            lineRepository.deleteById(id);
            return false;
        }
        return true;
    }
}
