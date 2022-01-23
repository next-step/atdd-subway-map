package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicatedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        validateDuplicatedLine(request);
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
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

    private void validateDuplicatedLine(LineRequest request) {
        boolean existsLine = lineRepository.existsLineByName(request.getName());
        if(existsLine) {
            throw new DuplicatedException("중복된 라인을 생성할 수 없습니다.");
        }
    }
}
