package nextstep.subway.line.application;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
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
        Line persistLine;
        try {
            persistLine = lineRepository.save(request.toLine());
        } catch (PersistenceException exception) {
            throw new ApplicationException(ApplicationType.KEY_DUPLICATED);
        }

        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {
        Optional<Line> line = lineRepository.findOneById(id);

        return line.map(LineResponse::of).orElseThrow(() -> new ApplicationException(ApplicationType.CONTENT_NOT_FOUND));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Optional<Line> persistLine = lineRepository.findOneById(id);

        Line line = persistLine.orElseThrow(() -> new ApplicationException(ApplicationType.CONTENT_NOT_FOUND));
        line.update(lineRequest.toLine());
        return  LineResponse.of(line);
    }
}
