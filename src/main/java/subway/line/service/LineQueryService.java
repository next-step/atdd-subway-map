package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.exception.NonExistentLineException;
import subway.line.payload.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineQueryService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    public LineQueryService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        Set<Long> stationsIds = lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .collect(Collectors.toSet());

        Map<Long, Station> stationMap = stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));

        return lines.stream()
                .map(line ->
                        LineResponse.from(line,
                                stationsIds.stream()
                                        .map(stationMap::get)
                                        .collect(Collectors.toList()))
                ).collect(Collectors.toList());

    }

    public LineResponse getLine(final Long id) {
        Line line = getById(id);
        return LineResponse.from(line, getLineStations(line));

    }

    private Line getById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NonExistentLineException("존재하지 않는 지하철 노선입니다."));
    }

    private List<Station> getLineStations(final Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }
}
