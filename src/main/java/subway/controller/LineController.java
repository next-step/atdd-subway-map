package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineResponse;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.StationResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping()
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse line = lineService.saveLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/line/" + line.getId())).body(line);
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLines(){
        List<LineResponse> temp = lineService.findAllLines();
        LineResponse lineResponse = temp.get(0);

        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @PatchMapping()
    public ResponseEntity<LineResponse> updateLine(@RequestBody LineUpdateRequest lineUpdateRequest){

        LineResponse line = lineService.updateLine(lineUpdateRequest);
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id){
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

}
