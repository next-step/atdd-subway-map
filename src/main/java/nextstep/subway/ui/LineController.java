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
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineSaveResponse> createLine(@RequestBody final LineSaveRequest lineRequest) {
        LineSaveResponse line = lineService.saveLine(lineRequest);
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
}
