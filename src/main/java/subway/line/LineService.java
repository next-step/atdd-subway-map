package subway.line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    @Autowired
    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineRequest) {
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
        return createLineResponse(lineRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }
}
