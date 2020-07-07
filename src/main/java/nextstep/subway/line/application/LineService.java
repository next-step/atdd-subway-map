package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line saveLine(LineRequest request) {
        return lineRepository.save(request.toLine());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Long> stationIds = line.getOrderLineStations().stream()
                .map(it -> it.getStationId())
                .collect(Collectors.toList());

        Map<Long, Station> stations = stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(it -> it.getId(), Function.identity()));

        List<LineStationResponse> lineStationResponses = toLineStationResponse(line, stations);

        return LineResponse.of(line, lineStationResponses);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public List<LineStationResponse> toLineStationResponse(Line line, Map<Long, Station> stations) {
        return line.getOrderLineStations().stream()
                .map(it -> LineStationResponse.of(it, StationResponse.of(stations.get(it.getStationId()))))
                .collect(Collectors.toList());
    }
}
