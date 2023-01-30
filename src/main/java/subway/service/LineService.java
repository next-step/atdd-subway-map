package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line saveLine(Line line) {
        return lineRepository.save(line);

    }

    public List<Line> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);

        if (!request.getColor().isEmpty()) {
            line.changeColor(request.getColor());
        }
        if (!request.getName().isEmpty()) {
            line.changeName(request.getName());
        }

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
