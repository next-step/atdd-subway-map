package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(Line line) {
        return LineResponse.from(lineRepository.save(line));
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse searchById(Long id) {
        return LineResponse.from(
                lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    @Transactional
    public void update(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.update(updateLineRequest.getName(), updateLineRequest.getColor());
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(Long id, Station upstreamStation, Station downstreamStation) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.addSection(upstreamStation, downstreamStation);
        return LineResponse.from(line);
    }

    @Transactional
    public void deleteSection(Long id, Station downStreamTerminusStation) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.deleteSectionByDownStreamTerminusStation(downStreamTerminusStation);
    }
}
