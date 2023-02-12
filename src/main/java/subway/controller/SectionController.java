package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.LineService;

import java.net.URI;

@RestController
public class SectionController {

    private final LineService lineService;

    public SectionController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> saveSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        SectionResponse sectionResponse = lineService.saveSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/"+lineId+"/sections/"+sectionResponse.getId()))
                .body(sectionResponse);
    }

    @DeleteMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
