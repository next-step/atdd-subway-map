package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line line = createLine(request);

        return LineResponse.of(lineRepository.save(line));
    }

    private Line createLine(LineRequest request) {
        List<Station> stations = stationRepository.findByIdIn(
            List.of(request.getUpStationId(), request.getDownStationId()));

        return new Line(request.getName(), request.getColor(), stations);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }
}
