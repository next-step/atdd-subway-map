package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.SectionRequest;
import subway.section.SectionService;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class LineSectionService {
    private final LineService lineService;
    private final SectionService sectionService;

    public LineSectionService(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @Transactional
    public LineResponse saveLineSection(LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return sectionService.saveSection(lineResponse.getId(), SectionRequest.toRequest(lineRequest)).getLine();
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        return lineService.updateLine(id, lineRequest);
    }

    @Transactional
    public void deleteLineById(Long id) {
        sectionService.deleteAllLineOfSection(id);

        lineService.deleteLineById(id);
    }

    public LineResponse getLineResponseById(Long id) {
        return lineService.getLineResponseById(id);
    }

    public List<LineResponse> findAllLines() {
        return lineService.findAllLines();
    }
}
