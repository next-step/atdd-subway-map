package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Station upStation = getStationById(lineCreateRequest.getUpStationId());
        Station downStation = getStationById(lineCreateRequest.getDownStationId());
        Line line = lineRepository.save(Line.of(
            lineCreateRequest.getName(),
            lineCreateRequest.getColor(),
            upStation,
            downStation,
            lineCreateRequest.getDistance()
        ));
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAllWithStations().stream()
            .map(LineResponse::new)
            .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findByIdWithStations(id).orElseThrow(() -> new RuntimeException("not found line"));
        return new LineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("not found line"));
        line.updateName(lineUpdateRequest.getName());
        line.updateColor(lineUpdateRequest.getColor());
    }

    @Transactional
    public void removeLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("not found line"));
        lineRepository.delete(line);
    }

    // TODO: custom exceoption & exception handler
    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new RuntimeException("not found station"));
    }


}
