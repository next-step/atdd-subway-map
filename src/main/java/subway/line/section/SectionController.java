package subway.line.section;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;
    @PostMapping
    public ResponseEntity<SectionResponse> registerSection(@PathVariable Long lineId,
        @RequestBody SectionRequest sectionRequest) {

        SectionResponse response = sectionService.registerSection(lineId, sectionRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> searchSections(@PathVariable Long lineId) {
        List<SectionResponse> sections = sectionService.findAllSections(lineId);

        return ResponseEntity.ok(sections);
    }
}
