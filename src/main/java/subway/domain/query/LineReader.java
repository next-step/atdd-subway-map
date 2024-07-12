package subway.domain.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.line.Line;
import subway.domain.entity.station.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;
import subway.domain.view.LineView;
import subway.domain.view.StationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LineReader {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public LineView.Main getOneById(Long id) {
        Line line = lineRepository.findByIdOrThrow(id);
        Map<Long, Station> stationMap = getStationMapByIds(
                line.getSections().stream()
                        .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                        .collect(Collectors.toSet())
        );
        return joinAndTransform(line, stationMap);
    }


    @Transactional(readOnly = true)
    public List<LineView.Main> getAllLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stationMap = getStationMapByIds(
                lines.stream()
                        .flatMap(line -> line.getSections().stream()
                                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                        )
                        .collect(Collectors.toSet())
        );
        return lines.stream()
                .map(line -> joinAndTransform(line, stationMap))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMapByIds(Iterable<Long> ids) {
        Map<Long, Station> stationMap = new HashMap<>();
        stationRepository.findAllById(ids).forEach((station -> stationMap.putIfAbsent(station.getId(), station)));
        return stationMap;
    }

    private LineView.Main joinAndTransform(Line line, Map<Long, Station> stationMap) {
        List<StationView.Main> allStations = line.getSections().stream()
                .flatMap(section -> Stream.of(stationMap.get(section.getUpStationId()), stationMap.get(section.getDownStationId()))
                .filter(Objects::nonNull)
                .map(station -> new StationView.Main(station.getId(), station.getName()))
        ).collect(Collectors.toList());

        return new LineView.Main(line.getId(), line.getName(), line.getColor(), allStations);
    }
}
