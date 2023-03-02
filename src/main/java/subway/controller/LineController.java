package subway.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.*;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok().body(lineService.findALlLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse lineResponse = lineService.findLineById(id);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchLine(@PathVariable Long id, @RequestBody LinePatchResponse linePatchResponse) {
        lineService.updateLineById(id, linePatchResponse);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = lineService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections")).body(sectionResponse);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSectionById(id, stationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
