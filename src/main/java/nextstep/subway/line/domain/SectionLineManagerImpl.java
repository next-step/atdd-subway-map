package nextstep.subway.line.domain;

import nextstep.subway.section.domain.manager.SectionLineManager;
import org.springframework.stereotype.Component;

@Component
public class SectionLineManagerImpl implements SectionLineManager {

    private final LineRepository lineRepository;

    public SectionLineManagerImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean isExistsByLine(Long lineId) {
        return lineRepository.existsById(lineId);
    }
}
