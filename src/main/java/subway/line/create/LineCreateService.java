package subway.line.create;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

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
        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        return LineCreatedResponse.from(lineRepository.save(line));
    }

    private Station findStationByStationId(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다. stationId: " + stationId));
    }
}
