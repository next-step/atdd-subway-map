package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.LineNotFoundException;
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
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    /*
        위의 findAllLines()는 lineRepository.findAll()을 변수에 담지 않고 바로 사용하였고,
        아래의 findLine()의 경우는 lineRepository.findById()를 변수에 담고 사용하였습니다.
        저희 회사의 경우 findAllLines() 처럼 변수에 담지 않고 바로 사용하는 것을 지향하는데요,
        이 2가지 방법에 대해 어떤 견해를 가지고 계신지 궁금합니다.
     */

    /*
        자바 플레이그라운드에서 Exception의 경우 아래처럼 커스텀한 Exception을 만들어서 사용하는 경우도 있었는데요,
        언제 커스텀한 Exception을 만들어서 사용하는지 견해를 듣고 싶습니다.
     */
    public LineResponse findLineById(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        return createLineResponse(line);
    }

    public LineResponse updateLine(Long lineId, LineRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        line.update(request.getName(), request.getColor());
        return createLineResponse(line);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
