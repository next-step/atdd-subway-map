package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.exception.LineNotFoundException;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = new Station(stationService.findStationById(lineRequest.getUpStationId()));
        Station downStation = new Station(stationService.findStationById(lineRequest.getDownStationId()));

        Line line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        upStation.getId(),
                        downStation.getId(),
                        lineRequest.getDistance()));

        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return new LineResponse(lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                findStations(line.getUpStationId(), line.getDownStationId())
        );
    }

    private List<Station> findStations(Long upStationId, Long downStationId) {
        Station upStation = new Station(stationService.findStationById(upStationId));
        Station downStation = new Station(stationService.findStationById(downStationId));

        return List.of(upStation, downStation);
    }
}
