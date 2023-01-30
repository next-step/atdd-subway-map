package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        StationResponse upStation = stationService.findById(lineRequest.getUpStationId());
        StationResponse downStation = stationService.findById(lineRequest.getDownStationId());

        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            upStation.getId(),
            downStation.getId(),
            lineRequest.getDistance()
        ));

        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            List.of(upStation, downStation)
        );
    }

    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        List<Station> stations = stationService.findAllByIds(List.of(line.getUpStation(), line.getDownStation()));
        return new LineResponse(line, stations);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new LineNotFoundException(id));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(line -> {
                List<Station> stations = stationService.findAllByIds(List.of(line.getUpStation(), line.getDownStation()));
                return new LineResponse(line, stations);
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
