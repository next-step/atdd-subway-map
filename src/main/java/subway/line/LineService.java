package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toEntity());
        List<Station> stations = stationRepository.findAll().stream()
                .filter(station -> line.stationIds().contains(station.getId()))
                .collect(Collectors.toList());
        return new LineResponse(line, stations);
    }

    public List<LineResponse> findAllLines() {
        List<Station> stations = stationRepository.findAll();
        return lineRepository.findAll().stream()
                .map(line -> new LineResponse(
                                line,
                                stations.stream()
                                        .filter(station -> line.stationIds().contains(station.getId()))
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        lineRepository.deleteById(id);
    }
}
