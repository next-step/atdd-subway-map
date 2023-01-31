package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line newLine = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance());
        Line savedLine = lineService.saveLine(newLine, lineRequest.getUpStationId(), lineRequest.getDownStationId());

        return ResponseEntity.created(URI.create("/lines/" + savedLine.getId())).body(LineResponse.of(savedLine));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.findAllLines()
                .stream().map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        Line line = lineService.findLine(id);
        return ResponseEntity.ok().body(LineResponse.of(line));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest.getColor(), lineRequest.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        Section section = lineService.addSection(id, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        return ResponseEntity.created(URI.create("/lines/" + section.getId())).body(SectionResponse.of(section));
    }

    @DeleteMapping(value = "/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long sectionId) {
        lineService.deleteSection(id, sectionId);
        return ResponseEntity.noContent().build();
    }

}
