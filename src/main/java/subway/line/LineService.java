package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::createLineResponse)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        lineRepository.findById(id)
                .ifPresent(line -> {
                    line.update(lineRequest.getName(), lineRequest.getColor());
                    lineRepository.save(line);
                });
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.findById(id)
                .ifPresent(lineRepository::delete);
    }
}
