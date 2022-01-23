package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.dto.LineResponse;
import nextstep.subway.line.domain.model.Line;
import nextstep.subway.line.domain.repository.LineRepository;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.common.exception.EntityNotFoundException;
import nextstep.subway.common.exception.OptionalException;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        verifyExistsByName(request.getName()).verify();

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.from(line);
    }

    private OptionalException<DuplicateColumnException> verifyExistsByName(String name) {
        if (lineRepository.existsByName(name)) {
            return OptionalException.of(
                new DuplicateColumnException(ColumnName.LINE_NAME)
            );
        }
        return OptionalException.empty();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toList());
    }

    public LineResponse findById(long id) {
        return lineRepository.findById(id)
                             .map(LineResponse::from)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void edit(long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        if (line.notMatchName(lineRequest.getName())) {
            verifyExistsByName(lineRequest.getName()).verify();
        }
        line.edit(lineRequest.getName(), lineRequest.getColor());
    }

    public void delete(long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }
}
