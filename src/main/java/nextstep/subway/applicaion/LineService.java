package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveStation(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest));
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations()
        );
    }

    public List<LineResponse> findAllStations() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse findById(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        return new LineResponse(
                line.get().getId(),
                line.get().getName()
        );
    }
}
