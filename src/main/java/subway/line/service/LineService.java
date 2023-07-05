package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.exception.NotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(lineRequest.toEntity());

        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {

        return lineRepository.findAll()
            .stream().map(LineResponse::new)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(long id) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));

        return new LineResponse(line);
    }

    @Transactional
    public LineResponse updateLine(long id, LineRequest lineRequest) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        line.update(lineRequest);

        return new LineResponse(line);
    }

    public void deleteLine(long id) {

        Line line = lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        lineRepository.delete(line);
    }
}
