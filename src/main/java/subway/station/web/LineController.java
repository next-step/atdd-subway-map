package subway.station.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.service.LineService;
import subway.station.service.dto.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineSaveResponse> saveLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineSaveResponse line = lineService.save(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineFindAllResponse>> viewLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineFindByLineResponse> findLineById(@PathVariable Long id) {
        LineFindByLineResponse line = lineService.findById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineUpdateResponse> updateLine(@PathVariable Long id, @RequestBody LineUpdateResponse updateLineRequest) {
        LineUpdateResponse line = lineService.update(id, updateLineRequest.getName(), updateLineRequest.getColor());
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
