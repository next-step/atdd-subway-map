package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long id, @RequestBody SectionRequest request) {
        Long sectionId = sectionService.saveSection(id, request);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections/" + sectionId)).build();
    }
}
