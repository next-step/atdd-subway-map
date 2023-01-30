package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.LineNotFoundException;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        Line newLine = lineRepository.save(line);
        return new LineResponse(newLine.getId(), newLine.getName(), newLine.getColor(),
                makeStationResponses(upStation, downStation));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> {
            return new LineResponse(line.getId(), line.getName(), line.getColor(),
                    makeStationResponses(line.getUpStation(), line.getDownStation()));
        }).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), makeStationResponses(line.getUpStation(), line.getDownStation()));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);

        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        line.changeLineInfo(lineRequest.getName(), lineRequest.getColor(),
                upStation, downStation, lineRequest.getDistance());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private static List<StationResponse> makeStationResponses(Station line, Station line1) {
        return List.of(StationResponse.of(line), StationResponse.of(line1));
    }
}
