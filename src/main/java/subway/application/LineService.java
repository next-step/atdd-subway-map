package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());

        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            upStation,
            downStation,
            lineRequest.getDistance()
        ));

        return createLineResponse(line);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new StationNotFoundException(id));
    }

    private LineResponse createLineResponse(Line line) {
        List<Station> stations = List.of(line.getUpStation(), line.getDownStation());

        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList())
        );
    }

    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return createLineResponse(line);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new LineNotFoundException(id));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }
}
