package subway.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.SectionResponse;
import subway.service.LineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse response = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> response = lineService.getLines();
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineResponse response = lineService.getLine(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> updateLine(
        @PathVariable Long id, @RequestBody LineRequest request
    ) {
        LineResponse response = lineService.updateLine(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(
        @PathVariable Long id
    ) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> createSections(
        @PathVariable Long id, @RequestBody SectionRequest request
    ) {
        SectionResponse response = lineService.createSection(id, request);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections")).body(response);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> deleteSections(
        @PathVariable Long id, @RequestParam Long stationId
    ) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

}