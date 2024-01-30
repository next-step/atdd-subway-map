package subway.line;

import org.springframework.stereotype.Service;
import subway.station.Station;
import subway.station.StationNotFoundException;
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

    public LineResponse createLine(LineRequest lineRequest) throws StationNotFoundException {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(StationNotFoundException::new);
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

    public LineResponse showLine(Long id) throws LineNotFoundException {
        return createLineResponse(lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    public void updateLine(Long id, UpdateLineRequest updateLineRequest) throws LineNotFoundException {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.setName(updateLineRequest.getName());
        line.setColor(updateLineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
