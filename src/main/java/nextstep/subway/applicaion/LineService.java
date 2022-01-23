package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineIncludingStationsResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineIncludingStationsResponse> findAllLines() {
        List<Line> lines = lineRepository.findAllWithStations();
        return lines.stream()
                .map(LineIncludingStationsResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineIncludingStationsResponse findById(Long id) {
        return new LineIncludingStationsResponse(lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 역을 찾을 수 없습니다. id = " + id)));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 역을 찾을 수 없습니다. id = " + id));
        line.update(lineRequest.getName(), lineRequest.getColor());
    }
}
