package lines;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
            new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
                lineRequest.getDownStationId(), lineRequest.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse selectLineById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
        return createLineResponse(line);
    }

    public LineResponse updateLineById(LineRequest lineRequest, Long id) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
        line.setUpStationId(lineRequest.getUpStationId());
        line.setDownStationId(lineRequest.getDownStationId());
        line.setDistance(lineRequest.getDistance());
        return createLineResponse(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor()
        );
    }
}
