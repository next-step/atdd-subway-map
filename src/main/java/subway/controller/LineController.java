package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineModifyRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import subway.service.LineService;


import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) throws Exception{
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) throws Exception{
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineRequest) throws Exception{
        return ResponseEntity.ok().body(lineService.modifyLine(id, lineRequest));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
