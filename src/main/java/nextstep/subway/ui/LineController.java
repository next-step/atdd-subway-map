package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineSaveResponse> createLine(@RequestBody final LineSaveRequest lineRequest) {
        final LineSaveResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineReadAllResponse>> findAllLine() {
        return ResponseEntity.ok(lineService.findAllLine());
    }

    @GetMapping(value = {"{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineReadResponse> findLine(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLine(id));
    }

    @PutMapping(value = {"{id}"})
    public ResponseEntity<Void> updateLine(@RequestBody final LineUpdateRequest lineUpdateRequest,
                                           @PathVariable final Long id) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = {"{id}"})
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "{id}/sections")
    public ResponseEntity<SectionSaveResponse> addSection(@PathVariable final Long id,
                                                          @RequestBody final SectionAddRequest sectionAddRequest) {
        final SectionSaveResponse section = lineService.addSection(id, sectionAddRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections?stationId=" + section.getDownStationId())).body(section);
    }

    @DeleteMapping(value = "{id}/sections")
    public ResponseEntity<Void> removeSection(@PathVariable final Long id,
                                              @RequestParam final Long stationId) {
        lineService.removeSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
