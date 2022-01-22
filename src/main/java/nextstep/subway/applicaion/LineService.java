package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineDetailResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
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
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    public LineDetailResponse getLine(Long id) {
        return LineDetailResponse.from(lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 노선입니다.")
                ));
    }

    public void patchLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 노선입니다."));

        line.modify(lineRequest.getName(), lineRequest.getColor());
    }
}
