package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.ModifyLineResponse;
import subway.line.service.LineService;
import subway.section.dto.AddSectionRequest;
import subway.section.dto.AddSectionResponse;
import subway.section.dto.SectionResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest createLineRequest) {
        LineResponse createdLine = lineService.createLine(createLineRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLine);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> findLines() {
        List<LineResponse> lines = lineService.findAllLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long id) {
        LineResponse line = lineService.findLine(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<ModifyLineResponse> modifyLine(@PathVariable Long id, @RequestBody ModifyLineRequest modifyLineRequest) {
        ModifyLineResponse modifiedLine = lineService.modifyLine(id, modifyLineRequest);
        return ResponseEntity.ok(modifiedLine);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<AddSectionResponse> addSection(@PathVariable Long lineId, @RequestBody AddSectionRequest addSectionRequest
    ) {
        AddSectionResponse addSectionResponse = lineService.addSection(lineId, addSectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addSectionResponse);
    }

    @GetMapping("/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> findSections(@PathVariable Long lineId) {
        List<SectionResponse> sections = lineService.findSections(lineId);
        return ResponseEntity.ok(sections);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
