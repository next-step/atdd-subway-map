package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStationId(lineRequest.getUpStationId())
                .downStationId(lineRequest.getDownStationId())
                .distance(lineRequest.getDistance())
                .build());
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }


    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id)
                .map(this::createLineResponse)
                .orElseThrow(RuntimeException::new);
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        lineRepository.findById(id)
                .map(line -> line.updateLine(lineRequest))
                .orElseThrow(RuntimeException::new);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .upStationId(line.getUpStationId())
                .downStationId(line.getDownStationId())
                .distance(line.getDistance())
                .build();
    }
}
