package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.dto.SectionCreateRequest;
import subway.line.model.Line;
import subway.line.model.Section;
import subway.line.repository.SectionRepository;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public Section appendSection(Line line, SectionCreateRequest request) {
        // TODO: 구현해라!
        return null;
    }
}
