package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.section.Section;
import subway.domain.sectionstation.SectionStation;
import subway.dto.line.*;
import subway.repository.SectionRepository;
import subway.repository.SectionStationRepository;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("")
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
        CreateLineResponse response = lineService.createLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<ReadLinesResponse>> readLines() {
        return ResponseEntity
                .ok(lineService.readLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLineResponse> readLine(@PathVariable Long id) {
        return ResponseEntity
                .ok(lineService.readLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
                                           @RequestBody UpdateLineRequest request) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> extendLine(@PathVariable Long id,
                                           @RequestBody ExtendLineRequest request) {
        lineService.extendLine(request.setLineId(id));
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> reduceLine(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.reduceLine(new ReduceLineRequest(id, stationId));
        return ResponseEntity
                .noContent()
                .build();
    }

}
