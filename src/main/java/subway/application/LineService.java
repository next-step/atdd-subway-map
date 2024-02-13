package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.application.dto.LineCreateRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Line line = lineRepository.save(lineCreateRequest.to());
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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));
    }

    public void updateLineById(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));
        line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
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
