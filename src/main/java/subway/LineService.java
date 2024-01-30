package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineResponse create(LineCreateRequest request) {
        Station upstation = stationRepository.findById(request.getUpstationId()).orElseThrow(EntityNotFoundException::new);
        Station downstation = stationRepository.findById(request.getDownstationId()).orElseThrow(EntityNotFoundException::new);

        Line line = LineCreateRequest.toEntity(request, upstation, downstation);

        lineRepository.save(line);

        List<StationResponse> stations =
                List.of(upstation, downstation).stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList());

        return LineResponse.from(line, stations);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(line -> {
                    List<StationResponse> stations = List.of(line.getUpstation(), line.getDownstation())
                            .stream()
                            .map(StationResponse::from)
                            .collect(Collectors.toList());
                    return LineResponse.from(line, stations);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        List<StationResponse> stations = List.of(line.getUpstation(), line.getDownstation())
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineResponse.from(line, stations);
    }

    @Transactional
    public LineResponse update(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        line.updateName(request.getName());
        line.updateColor(request.getColor());

        List<StationResponse> stations = List.of(line.getUpstation(), line.getDownstation())
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return LineResponse.from(line, stations);
    }

    public void delete(Long id) {
        lineRepository.deleteById(id);
    }
}
