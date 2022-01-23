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
}
