package subway.linesection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("lines/{lineId}/sections")
public class LineSectionController {
    private final LineSectionService lineSectionService;

    public LineSectionController(LineSectionService lineSectionService) {
        this.lineSectionService = lineSectionService;
    }


    @PostMapping
    public ResponseEntity<LineSectionResponse> addSection(@PathVariable Long lineId, @RequestBody LineSectionRequest request) {
        lineSectionService.addSection(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections"))
                .body(null);
    }
}
