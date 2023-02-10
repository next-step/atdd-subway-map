package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.service.LineService;

@RestController
public class SectionController {

    private final LineService lineService;

    public SectionController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> saveSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        lineService.saveSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
