package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }
    @PostMapping("/lines")
    public ResponseEntity<LineResponse> addLine(@RequestBody LineRequest request){
        LineResponse response= lineService.makeLine(request);
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines(){
        List<LineResponse> response = lineService.getLines();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable("id") Long id){
        LineResponse response = lineService.getLine(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable Long id, @RequestBody LineRequest request){
        LineResponse update = lineService.update(id, request);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping("/lines/{id}")
    public ResponseEntity  delLine(@PathVariable Long id){
        lineService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
