package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;

    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationByStationId(lineRequest.getUpStationId());
        Station downStation = stationService.findStationByStationId(lineRequest.getDownStationId());

        final var line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        upStation,
                        downStation,
                        lineRequest.getDistance()));

        return createLineResponse(null);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        final var line = findById(id);

        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        final var line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        final var line = findById(id);
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    private LineResponse createLineResponse(Line line) {
        List<Station> stations = line.getSections().getSections().stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream()
                        .map(StationResponse::createStationResponse)
                        .collect(Collectors.toList()));
    }
}
