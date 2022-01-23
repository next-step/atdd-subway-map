package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicatedLineException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        // Entity to LineResponse(DTO)
        return new LineResponse(line);
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(toList());
    }

    public LineResponse getLine(Long lineId) {
        Line findLine = getLineById(lineRepository, lineId);
        return new LineResponse(findLine);
    }

    public void modifyLine(Long lineId, LineRequest lineRequest) {
        Line findLine = getLineById(lineRepository, lineId);
        findLine.chageLine(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line findLine = getLineById(lineRepository, lineId);
        lineRepository.delete(findLine);
    }

    private Line getLineById(LineRepository lineRepository, Long lineId) {
        return lineRepository
                .findById(lineId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
