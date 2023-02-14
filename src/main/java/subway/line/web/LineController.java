package subway.line.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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

import subway.line.application.LineService;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.section.dto.SectionRequest;
import subway.line.section.dto.SectionResponse;

@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(lineService.findLineById(lineId));
    }

    @PutMapping(value = "/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @Valid @RequestBody LineRequest lineRequest) {
        lineService.updateLineById(lineId, lineRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId,
        @Valid @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = lineService.createSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + section.getId())).body(section);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
