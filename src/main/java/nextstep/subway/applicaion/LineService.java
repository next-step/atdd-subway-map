package nextstep.subway.applicaion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicatedElementException;
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
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicatedElementException("중복된 지하철 라인 이름은 넣을 수 없습니다.");
        }

        Line line = lineRepository.save(request.toEntity());
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(NoSuchElementException::new);

        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long lineId, LineRequest lineRequest) {
        Line savedLine = lineRepository.findById(lineId)
            .orElseThrow(NoSuchElementException::new);
        savedLine.update(lineRequest.toEntity());

        return LineResponse.of(savedLine);
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
