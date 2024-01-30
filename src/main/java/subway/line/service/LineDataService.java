package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.exception.LineException;
import subway.line.LineRepository;
import subway.line.dto.LineResponse;
import subway.station.Station;
import subway.station.StationDataService;
import subway.station.StationRepository;
import subway.station.StationResponse;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineDataService {

    private final StationDataService stationDataService;
    private final LineRepository lineRepository;

    public LineDataService(StationDataService stationDataService, LineRepository lineRepository) {
        this.stationDataService = stationDataService;
        this.lineRepository = lineRepository;
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 노선입니다."));
    }

    public LineResponse mappingToLineResponse(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(stationDataService::mappingToStationResponse).collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
