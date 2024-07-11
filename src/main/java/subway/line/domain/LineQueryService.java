package subway.line.domain;

import java.util.List;

import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.application.dto.SectionRequest;

public interface LineQueryService {
    LineResponse findLineById(Long id);

    List<LineResponse> findAllLines();
}
