package subway.line.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
            new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));

        List<StationResponse> stations = getStationResponsesByStationIds(List.of(line.getUpStationId(), line.getDownStationId()));

        return createLineResponse(line, stations);
    }

    public List<LineResponse> findAllLines() {
        Map<String, List<Line>> groupedLines = lineRepository.findAll().stream()
            .collect(Collectors.groupingBy(Line::getName));

        return groupedLines.values().stream()
            .map(this::createGroupedLineResponse)
            .collect(Collectors.toList());
    }

    private List<StationResponse> getStationResponsesByStationIds(Iterable<Long> stationIds) {
        Map<Long, Station> stationMap = stationRepository.findAllById(stationIds).stream()
            .collect(Collectors.toMap(Station::getId, station -> station));

        return stationMap.values().stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line, List<StationResponse> stations) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations
        );
    }

    private LineResponse createGroupedLineResponse(List<Line> lines) {
        Line representativeLine = lines.get(0);

        List<Long> stationIds = lines.stream()
            .flatMap(line -> Stream.of(line.getUpStationId(), line.getDownStationId()))
            .distinct()
            .collect(Collectors.toList());

        List<StationResponse> stations = getStationResponsesByStationIds(stationIds);

        return createLineResponse(representativeLine, stations);
    }

}
