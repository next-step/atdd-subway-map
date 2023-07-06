package subway.section.application;

import org.springframework.stereotype.Service;
import subway.line.domain.LineRepository;
import subway.section.domain.SectionRepository;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;

@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public SectionResponse registerSection(Long lineId, SectionRequest sectionRequest) {
        return null;
    }
}
