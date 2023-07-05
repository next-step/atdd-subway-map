package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(new Line(
                request.getName(),
                request.getColor(),
                stationRepository.getReferenceById(request.getUpStationId()),
                stationRepository.getReferenceById(request.getDownStationId())
        ));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        return createLineResponse(line);
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        line.setName(request.getName());
        line.setColor(request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                Arrays.asList(
                        new StationResponse(line.getUpStation().getId(), line.getUpStation().getName()),
                        new StationResponse(line.getDownStation().getId(), line.getDownStation().getName())
                ));
    }
}
