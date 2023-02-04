package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.output.LineLoadRepository;
import subway.domain.Line;

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
        return lineLoadRepository.loadLine(loadLineId);
    }

    @Override
    public List<Line> loadLines() {
        return lineLoadRepository.loadLines();
    }

}
