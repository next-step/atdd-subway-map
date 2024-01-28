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

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        final Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 상행종점역 입니다."));
        final Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 하행종점역 입니다."));

        final Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation);
        final Line savedLine = lineRepository.save(line);
        return new LineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }
}
