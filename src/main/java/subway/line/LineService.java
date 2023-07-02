package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

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
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("상행종점역을 찾을 수 없습니다. (upStationId: %d)", request.getUpStationId())));

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getDownStationId())));

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return LineResponse.of(lineRepository.findAll());
    }
}
