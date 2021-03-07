package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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

    public List<LineResponse> getLines(){
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        return LineResponse.of(getLineById(id));
    }

    public LineResponse updateLine(Long id, LineRequest request){
        Line line = getLineById(id);
        line.update(request.toLine());
        return LineResponse.of(line);
    }

    private Line getLineById(Long id){
        return lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
