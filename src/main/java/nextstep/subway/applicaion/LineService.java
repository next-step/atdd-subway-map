package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.ExceptionMessages;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
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
        Line savedLine = lineRepository.save(lineRequest.toEntity());
        return LineResponse.convertedByEntity(savedLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::convertedByEntity).collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(()->new RuntimeException(ExceptionMessages.NO_LINE_EXCEPTION_MESSAGE));
        return LineResponse.convertedByEntity(line);
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest, long lineId) {
        Line line = lineRequest.toEntity(lineId);
        Line updatedLine = lineRepository.save(line);
        return LineResponse.convertedByEntity(updatedLine);
    }

    @Transactional
    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }
}
