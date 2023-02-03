package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.output.LineLoadRepository;
import subway.application.service.input.LineLoadUseCase;
import subway.domain.Line;
import subway.domain.NotFoundLineException;

import java.util.List;

@Service
@Transactional(readOnly = true)
class LineLoadService implements LineLoadUseCase {

    private final LineLoadRepository lineLoadRepository;

    public LineLoadService(LineLoadRepository lineLoadRepository) {
        this.lineLoadRepository = lineLoadRepository;
    }

    @Override
    public Line loadLine(Long loadLineId) {
        return lineLoadRepository.loadLine(loadLineId)
            .orElseThrow(() -> new NotFoundLineException(String.format("요청한 Line 을 찾지 못했습니다 requested lineId: %d", loadLineId)));
    }

    @Override
    public List<Line> loadLines() {
        return lineLoadRepository.loadLines();
    }

}
