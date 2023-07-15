package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwaySectionController {

    private SubwaySectionService subwaySectionService;

    public SubwaySectionController(SubwaySectionService subwaySectionService) {
        this.subwaySectionService = subwaySectionService;
    }

    @PostMapping("/subway-sections/{lineId}")
    public ResponseEntity<SubwaySectionResponse> createSubwaySection(@PathVariable Long lineId,
            @RequestBody SubwaySectionRequest sectionRequest) {
        SubwaySectionResponse subwaySectionResponse = subwaySectionService.createSubwaySection(lineId, sectionRequest);

        return ResponseEntity.ok().body(subwaySectionResponse);
    }

    @PostMapping("/subway-sections/register")
    public ResponseEntity<SubwaySectionResponse> registerSubwaySection(
            @RequestBody SubwaySectionRequest sectionRequest) {
        SubwaySectionResponse subwaySectionResponse = subwaySectionService.registerSubwaySection(sectionRequest);

        return ResponseEntity.ok(subwaySectionResponse);
    }

}