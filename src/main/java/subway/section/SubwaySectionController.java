package subway.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class SubwaySectionController {

    private SubwaySectionService subwaySectionService;

    public SubwaySectionController(SubwaySectionService subwaySectionService) {
        this.subwaySectionService = subwaySectionService;
    }

    @PostMapping("/subway-sections")
    public ResponseEntity<SubwaySectionResponse> createSubwaySection(
            @RequestBody SubwaySectionRequest subwaySectionRequest) {
        SubwaySectionResponse subwaySectionResponse = subwaySectionService.createSubwaySection(subwaySectionRequest);

        return ResponseEntity.created(URI.create("/subway-sections/" + subwaySectionResponse.getId()))
                .body(subwaySectionResponse);
    }
}