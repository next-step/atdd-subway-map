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
        LineEntity lineEntity = lineRepository.save(new LineEntity(
                request.getName(),
                request.getColor(),
                stationRepository.getReferenceById(request.getUpStationId()),
                stationRepository.getReferenceById(request.getDownStationId())
        ));

        return createLineResponse(lineEntity);
    }

    private LineResponse createLineResponse(LineEntity lineEntity) {
        return new LineResponse(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                Arrays.asList(
                        new StationResponse(lineEntity.getUpStation().getId(), lineEntity.getUpStation().getName()),
                        new StationResponse(lineEntity.getDownStation().getId(), lineEntity.getDownStation().getName())
                ));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        LineEntity lineEntity = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        return createLineResponse(lineEntity);
    }
}
