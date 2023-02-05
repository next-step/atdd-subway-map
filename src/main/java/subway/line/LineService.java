package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId()).toEntity();
        Station downStation = stationService.findStationById(request.getDownStationId()).toEntity();

        Line line = lineRepository.save(request.toEntity(upStation, downStation));

        return LineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(line.getUpStation(), line.getUpStation()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream().map(line -> LineResponse.of(
                        line.getId(),
                        line.getName(),
                        line.getColor(),
                        List.of(line.getUpStation(), line.getDownStation())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = getLineById(id);
        List<Station> stations = List.of(line.getUpStation(), line.getDownStation());
        return LineResponse.of(line.getId(), line.getName(), line.getColor(), stations);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
