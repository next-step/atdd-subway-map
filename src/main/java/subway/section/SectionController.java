package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("lines/{lineId}/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        SectionResponse response = sectionService.create(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId+"/sections"))
                .body(response);
    }
}
