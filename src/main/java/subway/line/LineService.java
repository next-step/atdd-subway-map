package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()));

        List<Station> stations = List.of(getStationById(line.getUpStationId()), getStationById(line.getDownStationId()));

        return LineResponse.of(line.getId(), line.getName(), line.getColor(), stations);
    }

    @Transactional(readOnly = true)
    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<LineResponse> lineResponses = new ArrayList<>();
        List<Line> lines = lineRepository.findAll();

        for (Line line : lines) {
            List<Station> stations = List.of(getStationById(line.getUpStationId()), getStationById(line.getDownStationId()));
            lineResponses.add(LineResponse.of(line.getId(), line.getName(), line.getColor(), stations));
        }

        return lineResponses;
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        List<Station> stations = List.of(getStationById(line.getUpStationId()), getStationById(line.getDownStationId()));
        return LineResponse.of(line.getId(), line.getName(), line.getColor(), stations);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.update(request.toEntity());
    }
}
