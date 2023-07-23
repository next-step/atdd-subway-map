package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.code.LineValidateTypeCode;
import subway.line.domain.Line;
import subway.line.dto.request.LineRequest;
import subway.line.dto.request.SectionRequest;
import subway.line.dto.response.LineResponse;
import subway.line.dto.response.SectionResponse;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService LineService) {
        this.lineService = LineService;
    }

    @PostMapping()
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest LineRequest) {
        LineResponse Line = lineService.saveLine(LineValidateTypeCode.SAVE, LineRequest);
        return ResponseEntity.created(URI.create("/lines/" + Line.getId())).body(Line);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> inquiryLine(@PathVariable Long id) {
        LineResponse Line = lineService.inquiryLine(id);
        return ResponseEntity.ok().body(Line);
    }

    @GetMapping("")
    public ResponseEntity<List<LineResponse>> inquiryLines() {
        List<LineResponse> Lines = lineService.inquiryLines();
        return ResponseEntity.ok().body(Lines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineRequest LineRequest) {
        LineRequest.saveId(id);
        LineResponse Line = lineService.saveLine(LineValidateTypeCode.UPDATE, LineRequest);
        return ResponseEntity.ok().body(Line);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections")).body(lineService.updateSection(id, sectionRequest));
    }

    @GetMapping("/{id}/sections")
    public ResponseEntity<List<SectionResponse>> inquirySection(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.inquirySection(id));
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }


}
