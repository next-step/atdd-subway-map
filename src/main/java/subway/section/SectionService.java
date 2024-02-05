package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;

@Service
public class SectionService {

    private final LineService lineService;

    public SectionService(LineService lineService) {
        this.lineService = lineService;
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest request) {
        Section section = request.toEntity();
        Line line = lineService.findLineById(lineId);

        line.addSection(section);
        return createSectionResponse(section);
    }

    private static SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId());
    }

}
