package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository,
        final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        Line line = lineRepository.save(toLine(lineRequest));
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::new)
            .collect(Collectors.toList());
    }

    public LineResponse findALine(Long id) {
        return new LineResponse(
            lineRepository.findById(id)
                .orElseThrow(IllegalAccessError::new));
    }

    private Line toLine(final LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
            .orElseThrow(IllegalStateException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(IllegalStateException::new);

        return new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation,
            lineRequest.getDistance());
    }
}
