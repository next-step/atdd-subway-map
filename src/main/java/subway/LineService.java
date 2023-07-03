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
    public LineResponse saveLine(LineRequest request) {
        LineEntity lineEntity = lineRepository.save(new LineEntity(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId()
        ));

        return createLineResponse(lineEntity);
    }

    private LineResponse createLineResponse(LineEntity lineEntity) {
        Station upStation = stationRepository.getReferenceById(lineEntity.getUpStationId());
        Station downStation = stationRepository.getReferenceById(lineEntity.getDownStationId());

        return new LineResponse(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                Arrays.asList(
                        new StationResponse(upStation.getId(), upStation.getName()),
                        new StationResponse(downStation.getId(), downStation.getName())
                ));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }
}
