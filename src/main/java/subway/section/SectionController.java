package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable("lineId") Long lineId,
            @RequestBody SectionCreateRequest request) {
        SectionResponse sectionResponse = sectionService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + sectionResponse.getId())).body(sectionResponse);
    }

}
