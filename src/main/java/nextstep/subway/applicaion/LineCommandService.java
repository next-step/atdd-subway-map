package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineSaveRequest;
import nextstep.subway.applicaion.dto.LineSaveResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class LineCommandService {
    private LineRepository lineRepository;

    public LineCommandService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineSaveResponse saveLine(final LineSaveRequest request) {
        final Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineSaveResponse(line);
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
