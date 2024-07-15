package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse addLine(String lineName) {
        Line line = lineRepository.save(new Line(lineName));
        return new LineResponse(line.getId(), line.getName());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> loadLines() {
        return lineRepository.findAll().stream()
                .map(line -> new LineResponse(line.getId(), line.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse loadLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 노선이에요."));
        return new LineResponse(line.getId(), line.getName());
    }
}
