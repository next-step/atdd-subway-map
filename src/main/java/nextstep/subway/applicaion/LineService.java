package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
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
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream().map(LineResponse::new).collect(toList());
    }

    public LineResponse getLine(Long lineId) {
        return new LineResponse(lineRepository
                .findById(lineId)
                .orElseThrow(IllegalArgumentException::new));
    }

    public void modifyLine(Long lineId, LineRequest lineRequest) {
        Line findLine = lineRepository.findById(lineId)
                .orElseThrow(IllegalArgumentException::new);
        findLine.chageLine(lineRequest.getName(),lineRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line findLine = lineRepository.findById(lineId)
                .orElseThrow(IllegalArgumentException::new);

        lineRepository.delete(findLine);
    }
}
