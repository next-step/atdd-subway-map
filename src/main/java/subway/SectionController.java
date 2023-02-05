package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("")
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getId())).body(sectionResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionResponse> showSection(@PathVariable Long id) {
        return ResponseEntity.ok().body(sectionService.findSectionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        sectionService.deleteStationsById(id);
        return ResponseEntity.noContent().build();
    }
}
