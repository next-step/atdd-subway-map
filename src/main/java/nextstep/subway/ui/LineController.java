package nextstep.subway.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.application.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping(value = "/lines", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showAllLines() {
        List<LineResponse> list = lineService.findAllLines();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        LineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping(value = "/lines/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @Valid @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/lines/{id}/sections", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> createSection(@PathVariable Long id, @Valid @RequestBody SectionRequest sectionRequest) {
        sectionService.addSection(id, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam @Positive Long stationId) {
        sectionService.removeSectionById(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
