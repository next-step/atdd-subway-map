package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());
        return createLineResponse(line);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        return lineRepository.findById(id).map(this::createLineResponse).orElse(null);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationRepository.findByIdIn(List.of(
                                line.getUpStationId(),
                                line.getDownStationId()
                        )).stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
