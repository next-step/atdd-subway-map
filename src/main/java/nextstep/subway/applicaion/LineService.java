package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    // 지하철 노선 생성
    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return createLineResponse(line);
    }

    // 지하철 목록 조회
    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    // 지하철 노선 조회
    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        return lineRepository.findById(id)
                .map(this::createLineResponse)
                .orElseThrow(() -> new IllegalStateException("노선을 찾을 수 없습니다."));
    }

    // 지하철 노선 수정
    public void modifyLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("노선을 찾을 수 없습니다."));

        line.changInfo(lineRequest.getName(), lineRequest.getColor());
    }

    // 지하철 노선 삭제
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
