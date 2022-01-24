package nextstep.subway.line.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.section.application.manager.LineData;
import nextstep.subway.section.application.manager.SectionLineManager;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SectionLineManagerImpl implements SectionLineManager {

    private final LineRepository lineRepository;

    public SectionLineManagerImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean existsByLine(Long lineId) {
        return lineRepository.existsById(lineId);
    }

    @Override
    public Stream<LineData> getAllLines() {
        return lineRepository.findAll().stream()
                .map(LineData::of);
    }
}
