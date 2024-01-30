package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.LineResponse;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> add(@PathVariable("id") Long lineId, @RequestBody SectionAddRequest request) {
        return ResponseEntity.created(URI.create("/lines/")).body(sectionService.add(lineId, request));
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> delete(@PathVariable("id") Long lineId, @RequestParam("stationId") Long stationId) {
        sectionService.delete(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
