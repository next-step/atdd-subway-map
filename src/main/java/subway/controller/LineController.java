package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.UpdateLineRequest;
import subway.domain.view.LineView;
import subway.domain.query.LineReader;
import subway.domain.command.LineCommander;
import subway.controller.dto.CreateLineRequest;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineCommander lineService;
    private final LineReader lineReader;

    @PostMapping()
    public ResponseEntity<LineView.Main> createLine(@RequestBody CreateLineRequest request) {
        Long id = lineService.createLine(request.toCommand());
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(view);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineView.Main> updateLine(
            @PathVariable Long id,
            @RequestBody UpdateLineRequest request
    ) {
        lineService.updateLine(request.toCommand(id));
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.ok().body(view);
    }

    @GetMapping()
    public ResponseEntity<List<LineView.Main>> showLines() {
        List<LineView.Main> views = lineReader.getAllLines();
        return ResponseEntity.ok().body(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineView.Main> showLine(@PathVariable Long id) {
        LineView.Main view = lineReader.getOneById(id);
        return ResponseEntity.ok().body(view);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
