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
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
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

    public LineResponse findLine(Long lineId) {
        Line line = findLineById(lineId);
        List<Line> relatedLines = lineRepository.findByName(line.getName());

        List<StationResponse> stations = getStationResponsesByStationIds(getStationIds(relatedLines));

        return createLineResponse(line, stations);
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = findLineById(lineId);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = findLineById(lineId);
        lineRepository.delete(line);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new LineException(LineExceptionType.LINE_NOT_FOUND));
    }

    private List<StationResponse> getStationResponsesByStationIds(Iterable<Long> stationIds) {
        Map<String, Station> stationMap = stationRepository.findAllById(stationIds).stream()
            .collect(Collectors.toMap(
                Station::getName,
                station -> station,
                (existing, replacement) -> existing
            ));

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
        List<Long> stationIds = getStationIds(lines);
        return createLineResponse(lines.get(0), getStationResponsesByStationIds(stationIds));
    }

    private static List<Long> getStationIds(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> Stream.of(line.getUpStationId(), line.getDownStationId()))
            .distinct()
            .collect(Collectors.toList());
    }
}
