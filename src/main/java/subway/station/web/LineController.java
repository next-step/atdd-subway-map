package subway.station.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.service.LineService;
import subway.station.web.dto.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<SaveLineResponse> saveLine(@RequestBody SaveLineRequest saveLineRequest) {
        SaveLineResponse line = lineService.saveLine(saveLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<ViewLineResponse>> viewLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<FindLineResponse> findLineById(@PathVariable Long id) {
        FindLineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<UpdateLineResponse> updateLine(@PathVariable Long id, @RequestBody UpdateLineResponse updateLineRequest) {
        UpdateLineResponse line = lineService.update(id, updateLineRequest.getName(), updateLineRequest.getColor());
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
