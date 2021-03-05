package nextstep.subway.line.application;

import nextstep.subway.error.NameExistsException;
import nextstep.subway.error.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (checkExistsName(request.getName())) {
            throw new NameExistsException(request.getName());
        }

        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    private boolean checkExistsName(String name) {
        return lineRepository.findByName(name).isPresent();
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLine() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void updateLineById(LineRequest lineRequest, Long lineId) {
        Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));

        persistLine.update(lineRepository.save(lineRequest.toLine()));
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long lineId) {
        Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        lineRepository.delete(persistLine);
    }

}
