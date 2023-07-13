package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    public LineResponse createLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName());
        Line savedLine = lineRepository.save(line);
        return new LineResponse(savedLine.getId(), savedLine.getName());
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName()))
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Optional<Line> lineOptional = lineRepository.findById(id);
        Line line = lineOptional.orElseThrow(() -> new LineNotFoundException(id));
        return new LineResponse(line.getId(), line.getName());
    }

    public ModifyLineResponse modifyLine(Long id, ModifyLineRequest modifyLineRequest) {
        Optional<Line> lineOptional = lineRepository.findById(id);
        Line line = lineOptional.orElseThrow(() -> new LineNotFoundException(id));
        line.setName(modifyLineRequest.getName());
        Line modifiedLine = lineRepository.save(line);
        return new ModifyLineResponse(modifiedLine.getName());
    }

    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new LineNotFoundException(id);
        }
        lineRepository.deleteById(id);
    }
}
