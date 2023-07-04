package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/line")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/line/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines(){
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @PatchMapping("/line")
    public ResponseEntity<LineResponse> updateLine(@RequestBody LineRequest lineRequest){

        LineResponse line = lineService.updateLine(lineRequest);
        return ResponseEntity.ok().body(line);
    }
}
