package subway.line;

import org.springframework.stereotype.Service;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }
    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(line.getUpStation(), line.getDownStation()));
    }

    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation);

        lineRepository.save(line);
        return createLineResponse(line);
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse showLine(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow());
    }

    public void updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.setName(updateLineRequest.getName());
        line.setColor(updateLineRequest.getColor());
    }
}
