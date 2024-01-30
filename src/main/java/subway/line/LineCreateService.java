package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

@Transactional
@Service
public class LineCreateService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineCreateService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineCreatedResponse createLine(LineCreateRequest request) {
        Station upStation = findStationByStationId(request.getUpStationId());
        Station downStation = findStationByStationId(request.getDownStationId());

        Line line = new Line(
                request.getName(),
                request.getColor(),
                upStation.getId(),
                downStation.getId(),
                request.getDistance()
        );

        Line savedLine = lineRepository.save(line);

        return mapToResponse(savedLine, upStation, downStation);
    }

    private Station findStationByStationId(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다. stationId: " + stationId));
    }

    private LineCreatedResponse mapToResponse(Line savedLine, Station upStation, Station downStation) {
        LineCreatedStationResponse upStationResponse = new LineCreatedStationResponse(upStation.getId(), upStation.getName());
        LineCreatedStationResponse downStationResponse = new LineCreatedStationResponse(downStation.getId(), downStation.getName());
        return new LineCreatedResponse(
                savedLine.getId(),
                savedLine.getName(),
                savedLine.getColor(),
                List.of(upStationResponse, downStationResponse)
        );
    }
}
