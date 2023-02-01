package subway.line.station;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundLineException;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class LineStationService {

    private final LineRepository lineRepository;

    private final LineStationRepository lineStationRepository;

    private final StationRepository stationRepository;

    @Transactional
    public List<LineStation> registerLineStations(Long lineId, List<Long> stationIds) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(NotFoundLineException::new);

        List<Station> stations = stationRepository.findByIdIn(stationIds);

        List<LineStation> lineStations = stations.stream()
            .map(station -> new LineStation(line, station))
            .collect(Collectors.toList());

        List<LineStation> savedLineStations = lineStationRepository.saveAll(lineStations);

        savedLineStations.forEach(line::addLineStation);

        return savedLineStations;
    }
}
