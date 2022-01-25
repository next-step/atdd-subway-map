package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.line.LineDuplicateColorException;
import nextstep.subway.exception.line.LineDuplicateNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineSaveResponse saveLine(final LineSaveRequest lineRequest) {
        validateDuplicatedLineName(lineRequest.getName());
        validateDuplicatedLineColor(lineRequest.getColor());
        final Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        return new LineSaveResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineReadAllResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> new LineReadAllResponse(line, Collections.EMPTY_LIST))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineReadResponse findLine(final Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new LineReadResponse(line, Collections.EMPTY_LIST);
    }

    public void updateLine(final Long id, final LineUpdateRequest lineUpdateRequest) {
        validateDuplicatedLineName(lineUpdateRequest.getName());
        validateDuplicatedLineColor(lineUpdateRequest.getColor());
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void delete(final Long id) {
        lineRepository.deleteById(id);
    }

    private void validateDuplicatedLineName(final String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new LineDuplicateNameException();
        }
    }

    private void validateDuplicatedLineColor(final String lineColor) {
        if (lineRepository.existsByColor(lineColor)) {
            throw new LineDuplicateColorException();
        }
    }
}
