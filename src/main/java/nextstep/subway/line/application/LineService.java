package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
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
        final Line line = lineRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        List<LineStationResponse> lineStationResponses = this.toLineStationResponses(line.getLineStations());

        return lineRepository.findById(id)
                .map(it -> LineResponse.of(it, lineStationResponses))
                .orElseThrow(RuntimeException::new);
    }

    private List<LineStationResponse> toLineStationResponses(List<LineStation> lineStations) {
        final List<Long> lineStationIds = lineStations.stream()
                .map(it -> it.getStationId())
                .collect(Collectors.toList());

        final Map<Long, Station> stations = this.stationRepository.findAllById(lineStationIds).stream()
                .collect(Collectors.toMap(it -> it.getId(), it -> it));

        return lineStations.stream()
                .map(lineStation -> LineStationResponse.of(lineStation, stations.get(lineStation.getStationId())))
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
