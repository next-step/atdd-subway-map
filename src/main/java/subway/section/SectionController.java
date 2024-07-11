package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionFacade sectionFacade;

    public SectionController(SectionFacade sectionFacade) {
        this.sectionFacade = sectionFacade;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<?> createSections(@PathVariable Long id, @RequestBody SectionCreateRequest request){
        SectionCreateDTO dto = new SectionCreateDTO(id, request);
        Long sectionId = sectionFacade.createSection(dto);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections/" + sectionId )).build();
    }

    @GetMapping("/lines/{id}/sections")
    public ResponseEntity<SectionResponse> readSections(@PathVariable Long id){
        SectionResponse sectionResponse = sectionFacade.readSections(id);
        return ResponseEntity.ok(sectionResponse);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<?> deleteSections(@PathVariable Long id, @RequestParam Long stationId) {
        SectionDeleteDTO dto = new SectionDeleteDTO(id, stationId);
        sectionFacade.deleteSection(dto);
        return ResponseEntity.noContent().build();
    }
}
