package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.dto.SectionCreateRequest;
import subway.line.model.Line;

@Service
@RequiredArgsConstructor
public class LineSectionService {

    private final LineService lineService;

    private final SectionService sectionService;

    public void appendSection(final Long lineId, SectionCreateRequest request) {
        Line line = lineService.findLineById(lineId);
        sectionService.appendSection(line, request);
    }
}
