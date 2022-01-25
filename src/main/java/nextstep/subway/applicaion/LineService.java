package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineDetailResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.handler.error.custom.BusinessException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.handler.error.custom.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.handler.error.custom.ErrorCode.*;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new BusinessException(FOUND_DUPLICATED_NAME);
        }
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineDetailResponse getLine(Long id) {
        return LineDetailResponse.from(lineRepository.findById(id)
                .orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID)));
    }

    public void patchLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
        line.modify(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new BusinessException(LINE_NOT_FOUND_BY_ID);
        }
        lineRepository.deleteById(id);
    }
}
