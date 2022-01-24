package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
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

    public LineSaveResponse saveLine(final LineSaveRequest request) {
        final Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
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
        if (lineRepository.existsByName(lineUpdateRequest.getName())) {
            throw new EntityExistsException();
        }
        final Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void delete(final Long id) {
        lineRepository.deleteById(id);
    }
}
