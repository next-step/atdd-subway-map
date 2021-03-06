package nextstep.subway.line.application;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.NoResourceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lineList =  lineRepository.findAll();
        return lineList.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(long id) {
        Line line = lineRepository.findById(id).orElseThrow(()-> new NoResourceException("노선을 찾을수 없습니다."));
        return LineResponse.of(line);

    }

    public LineResponse modifyLine(long id,LineRequest lineRequest) {
        Line line  =  lineRepository.findById(id).orElseThrow(()-> new NoResourceException("노선을 찾을수 없습니다."));
        line.update(lineRequest.toLine());
        return LineResponse.of(line);
    }

    public void removeLine(long id) {
        lineRepository.deleteById(id);
    }
}
