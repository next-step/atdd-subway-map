package subway.section;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/lines")
@RestController
public class SectionController {

    private final SectionService sectionService;

    @GetMapping("/{id}/sections")
    public ResponseEntity<List<SectionResponse>> showSections(@PathVariable Long id) {
        return ResponseEntity.ok().body(sectionService.findAllSectionsByLineId(id));
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> addSection(@PathVariable Long id,
        @RequestBody SectionRequest request) {
        SectionResponse sectionResponse = sectionService.saveSection(id, request);
        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getId() + "/sections"))
            .body(sectionResponse);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        sectionService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

}
