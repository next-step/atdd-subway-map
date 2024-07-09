package subway.domain.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.domain.view.LineView;
import subway.domain.view.StationView;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LineReader {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public LineView.Main getOneById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_LINE));
        Map<Long, Station> stationMap = getStationMapByIds(List.of(line.getUpStationId(), line.getDownStationId()));
        return joinAndTransform(line, stationMap);
    }


    @Transactional(readOnly = true)
    public List<LineView.Main> getAllLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stationMap = getStationMapByIds(
                lines.stream()
                        .flatMap(line -> Stream.of(line.getUpStationId(), line.getDownStationId()))
                        .collect(Collectors.toSet())
        );
        return lines.stream().map(line -> joinAndTransform(line, stationMap)).collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMapByIds(Iterable<Long> ids) {
        Map<Long, Station> stationMap = new HashMap<>();
        stationRepository.findAllById(ids).forEach((station -> stationMap.putIfAbsent(station.getId(), station)));
        return stationMap;
    }

    private LineView.Main joinAndTransform(Line line, Map<Long, Station> stationMap) {
        List<StationView.Main> upDownStation = new ArrayList<>();
        Station upStation = stationMap.get(line.getUpStationId());
        Station downStation = stationMap.get(line.getDownStationId());
        if (upStation != null) {
            upDownStation.add(new StationView.Main(upStation.getId(), upStation.getName()));
        }

        if (downStation != null) {
            upDownStation.add(new StationView.Main(downStation.getId(), downStation.getName()));
        }

        return new LineView.Main(line.getId(), line.getName(), line.getColor(), upDownStation);
    }
}
