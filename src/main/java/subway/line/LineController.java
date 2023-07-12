package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.Section;
import subway.section.SectionCreateRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<Line> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        Line result = lineService.createLine(lineCreateRequest);

        return ResponseEntity
                .created(URI.create(format("/lines/%s", result.getId())))
                .body(result);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<Line>> getLines() {
        return ResponseEntity.ok(lineService.getLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<Line> getLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.getLineById(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody @Valid LineChangeRequest lineChangeRequest) {
        lineService.updateLine(id, lineChangeRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<Section> createSection(@PathVariable Long id, @RequestBody SectionCreateRequest sectionCreateRequest) {
        Section result = lineService.createSection(id, sectionCreateRequest);
        return ResponseEntity
                .created(URI.create(format("/sections/%s", result.getId())))
                .body(result);
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam("stationId") Long stationId) {
        lineService.deleteSection(id, stationId);

        return ResponseEntity.noContent().build();
    }
}
