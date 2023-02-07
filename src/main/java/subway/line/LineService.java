package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        List<Station> stations = findStations(lineRequest);

        Line line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        stations.get(0),
                        stations.get(1),
                        lineRequest.getDistance()));

        return new LineResponse(line);
    }

    private List<Station> findStations(LineRequest lineRequest) {
        List<Long> stationIds = List.of(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        return stationRepository.findAllById(stationIds);
    }
}
