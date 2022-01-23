package nextstep.subway.ui;

import nextstep.subway.applicaion.LineCommandService;
import nextstep.subway.applicaion.dto.LineSaveRequest;
import nextstep.subway.applicaion.dto.LineSaveResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineCommandController {
    private LineCommandService lineCommandService;

    public LineCommandController(LineCommandService lineService) {
        this.lineCommandService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineSaveResponse> createLine(@RequestBody final LineSaveRequest lineRequest) {
        LineSaveResponse line = lineCommandService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping(value = {"{id}"})
    public ResponseEntity<Void> updateLine(@RequestBody final LineUpdateRequest lineUpdateRequest,
                                           @PathVariable final Long id) {
        lineCommandService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = {"{id}"})
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
