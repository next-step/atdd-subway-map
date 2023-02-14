package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.station.Station;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Long upStationId = request.getUpStationId();
        Long downStationId = request.getDownStationId();

        Station upStation = stationService.getStationById(upStationId);
        Station downStation = stationService.getStationById(downStationId);
        Line requestLine = request.toEntity(upStation, downStation);
        Line line = lineRepository.save(requestLine);

        Section section = new Section(line, upStation, downStation, request.getDistance());
        line.addSection(section);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
            .stream()
            .map(
                line -> LineResponse.of(
                    line.getId(),
                    line.getName(),
                    line.getColor(),
                    line.getAllStations(),
                    line.getUpStation(),
                    line.getDownStation()
                )
            )
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = getLineById(id);
        List<Station> stations = line.getAllStations();

        return LineResponse.of(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations,
            line.getUpStation(),
            line.getDownStation()
        );
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = getLineById(id);
        line.update(
            request.getName(),
            request.getColor()
        );
    }

    @Transactional(readOnly = true)
    public Line getLineById(Long id) {
        return lineRepository
            .findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
