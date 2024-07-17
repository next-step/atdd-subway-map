package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.model.request.LineRequest;
import subway.model.request.StationRequest;
import subway.model.response.LineResponse;
import subway.model.response.StationResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService){
        this.lineService = lineService;
    }

    @PostMapping(value = "/line")
    public ResponseEntity<LineResponse> createSubwayLine(@RequestBody LineRequest lineRequest){
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showSubwayLines(){
        return ResponseEntity.ok().body(lineService.findAllLine());
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> showSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> modifySubwayLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.updateLine(id, lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
    @DeleteMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> deleteSubwayLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}
