package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId))
                             .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestParam final Map<String, String> queryString, @PathVariable Long lineId) {
        Long stationId = Long.valueOf(queryString.get("stationId"));
        sectionService.delete(lineId, stationId);
        return ResponseEntity.noContent()
                             .build();
    }
}
