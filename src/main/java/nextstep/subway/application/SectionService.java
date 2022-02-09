package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

    private final LineService lineService;

    public void deleteBy(Long lineId) {
        Line line = lineService.line(lineId);
        line.removeLastSection();
    }
}
