package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.exception.LineException;
import subway.line.LineRepository;
import subway.line.dto.LineResponse;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

import java.util.List;

@Service
public class LineDataService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public LineDataService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 노선입니다."));
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 역입니다."));
    }

    public LineResponse mappingToLineResponse(Line line) {
        List<StationResponse> stations = List.of(
                mappingToStationResponse(line.getUpStation()),
                mappingToStationResponse(line.getDownStation())
        );

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public StationResponse mappingToStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
