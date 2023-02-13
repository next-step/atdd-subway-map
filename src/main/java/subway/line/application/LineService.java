package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.infra.LineRepository;
import subway.line.controller.LineRequest;
import subway.line.controller.LineResponse;
import subway.line.exception.LineNotFoundException;
import subway.station.Station;
import subway.station.application.StationService;

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

        /*
        * presentation 에서 외부로 노출되는 클래스는 가장 변화가 일어나기 쉽기 때문에, use case 마다 분리하는 것이 좋은 경우가 많습니다.
        * use case 마다 분리
        * */
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                findStations(line.getUpStationId(), line.getDownStationId()));
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
