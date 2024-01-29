package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        Station upStation = this.stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("상행역이 존재하지 않습니다."));
        Station downStation = this.stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("상행역이 존재하지 않습니다."));
        Line line = this.lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = this.lineRepository.findAll();
        return lines.stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = this.lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 노선이 존재하지 않습니다."));
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        this.lineRepository.deleteById(id);
    }


    @Transactional
    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = this.lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 노선이 존재하지 않습니다."));
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(line.getUpStation(), line.getDownStation()));
    }
}
