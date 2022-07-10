package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line createdLine = lineRepository.save(lineRequest.toEntity());
        return new LineResponse(createdLine.getId(),createdLine.getName(),createdLine.getColor());
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return null;
    }
}
