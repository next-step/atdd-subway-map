package subway.domain.line.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.line.dto.request.CreateLineRequest;
import subway.domain.line.dto.request.CreateSectionRequest;
import subway.domain.line.dto.request.UpdateLineRequest;
import subway.domain.line.dto.response.LineResponse;
import subway.domain.line.dto.response.SectionResponse;
import subway.domain.line.service.SectionService;
import subway.domain.line.service.LineService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        LineResponse lineResponse = lineService.creteLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok().body(lineService.getLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLineById(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.getLineById(lineId);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long lineId, @RequestBody UpdateLineRequest request) {
        LineResponse lineResponse = lineService.updateLine(lineId, request);
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody CreateSectionRequest request) {
        SectionResponse section = sectionService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(section);
    }
}
