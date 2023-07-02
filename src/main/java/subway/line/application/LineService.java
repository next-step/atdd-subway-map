package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        lineRepository.save(line);

        return LineResponse.of(line);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("station이 없습니다. stationId=" + stationId));
    }

    public List<LineResponse> findAllStation() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findStation(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("line이 없습니다. lineId=" + id));

        return LineResponse.of(line);
    }
}
