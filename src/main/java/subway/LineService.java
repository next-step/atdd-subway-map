package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public Long createLine(CreateLineRequest request) {
        Line line = new Line(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
        lineRepository.save(line);
        return line.getId();
    }

    public List<Line> getLines() {
        return lineRepository.findAll();
    }
}
