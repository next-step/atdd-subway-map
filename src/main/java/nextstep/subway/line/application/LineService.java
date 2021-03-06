package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> allLines = lineRepository.findAll();

        return allLines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id){
        return LineResponse.of(lineRepository.getOne(id));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse updateLine(Long id, LineRequest request){
        Line originLine = lineRepository.getOne(id);
        Line updateLine = request.toLine();

        originLine.update(updateLine);

        lineRepository.save(originLine);

        return LineResponse.of(originLine);
    }

}
