package subway.domain.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.view.LineView;
import subway.domain.view.StationView;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineReader {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public LineView.Main getOneById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found Line"));
        List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));
        return joinAndTransform(line, stations);
    }


    @Transactional(readOnly = true)
    public List<LineView.Main> getAllLines() {
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAllById(
                lines.stream()
                        .flatMap(line -> Arrays.stream(new Long[] { line.getUpStationId(), line.getDownStationId() }))
                        .collect(Collectors.toSet())
        );
        return joinAndTransform(lines, stations);
    }

    private List<LineView.Main> joinAndTransform(List<Line> lines, List<Station> stations) {
        Map<Long, Station> stationMap = new HashMap<>();
        stations.forEach((station -> stationMap.putIfAbsent(station.getId(), station)));

        return lines.stream().map(line -> {
            List<StationView.Main> upDownStation = new ArrayList<>();
            Station upStation = stationMap.get(line.getUpStationId());
            Station downStation = stationMap.get(line.getDownStationId());
            if (upStation != null) {
                upDownStation.add(transform(upStation));
            }

            if (downStation != null) {
                upDownStation.add(transform(downStation));
            }

            return new LineView.Main(
                    line.getId(),
                    line.getName(),
                    line.getColor(),
                    upDownStation
            );
        }).collect(Collectors.toList());
    }

    private LineView.Main joinAndTransform(Line line, List<Station> stations) {
        Map<Long, Station> stationMap = new HashMap<>();
        stations.forEach((station -> stationMap.putIfAbsent(station.getId(), station)));

        List<StationView.Main> upDownStation = new ArrayList<>();
        Station upStation = stationMap.get(line.getUpStationId());
        Station downStation = stationMap.get(line.getDownStationId());
        if (upStation != null) {
            upDownStation.add(transform(upStation));
        }

        if (downStation != null) {
            upDownStation.add(transform(downStation));
        }

        return new LineView.Main(
                line.getId(),
                line.getName(),
                line.getColor(),
                upDownStation
        );
    }

    private StationView.Main transform(Station station) {
        return new StationView.Main(station.getId(), station.getName());
    }
}
