package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final SubwayLineRepository lineRepository;
    private final StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SubwayLineResponse saveLine(SubwayLineRequest request) {
        final Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(RuntimeException::new);
        final Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(RuntimeException::new);

        SubwayLine subwayLine = SubwayLine.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        return SubwayLineResponse.toResponse(lineRepository.save(subwayLine));
    }

    public List<SubwayLineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(SubwayLineResponse::toResponse)
                .collect(Collectors.toList());
    }
}
