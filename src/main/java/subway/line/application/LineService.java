package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.infra.LineRepository;
import subway.line.presentation.LinePatchRequest;
import subway.line.presentation.LineRequest;
import subway.line.presentation.LineResponse;
import subway.line.exception.LineNotFoundException;
import subway.station.Station;
import subway.station.application.StationService;
import subway.station.presentation.StationResponse;

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
        /*
         * TODO
         * StationResponse는 응용 계층에게 전달하기 위한 Dto인데
         * 도메인 간에 사용하는 Dto를 따로 만드는 편이 좋을 것 같다.
         *
         * StationResponse는 Station 도메인을 노출시키지 않는 목적으로
         * 표현 계층과 응용 계층 사이에서 협력을 위해 사용된다.
         *
         * 이와 같이 도메인을 노출시키지 않기 위해
         * LineService와 StationService 두 응용 계층에서 협력하기 위한 Dto를 만든다.
         *
         * 참고. Facade service를 두고 표현 계층에서는 Facade Service만 참조하는 형태로 구현
         * */
        Station upStation = new Station(stationService.findStationById(lineRequest.getUpStationId()));
        Station downStation = new Station(stationService.findStationById(lineRequest.getDownStationId()));

        Line line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        upStation.getId(),
                        downStation.getId(),
                        lineRequest.getDistance()));

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                findStations(line.getUpStationId(), line.getDownStationId())
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                findStations(line.getUpStationId(), line.getDownStationId())
        );
    }

    public void updateLineById(Long id, LinePatchRequest linePatchRequest) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        lineRepository.save(line.update(linePatchRequest.getName(), linePatchRequest.getColor()));
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
