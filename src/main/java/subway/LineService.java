package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public BuildLineResponse addLine(String lineName) {
        Line line = lineRepository.save(new Line(lineName));
        return new BuildLineResponse(line.getId(), line.getName());
    }
}
