package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line create(LineRequest request) {
        Line newLine = Line.of(request.getName(), request.getColor());
        return lineRepository.save(newLine);
    }


    public List<Line> getList() {
        return lineRepository.findAll();
    }

    public Line getById(Long id) {
        return getLine(id);
    }


    @Transactional
    public void update(Long id, LineRequest request) {
        Line before = getLine(id);
        Line line = Line.of(before.getId(), request.getName(), request.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.delete(getLine(id));
    }

    public Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("line is not existed by id > " + id));
    }
}
