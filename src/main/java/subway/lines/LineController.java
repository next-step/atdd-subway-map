package subway.lines;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LineController {
    private LineService lineService;
    
    public LineController(LineService lineService)
    {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest)
    {
        LineResponse lineResponse = this.lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines()
    {
        return ResponseEntity.ok().body(this.lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id)
    {
        return ResponseEntity.ok().body(this.lineService.findLine(id));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id)
    {
        this.lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable Long id, @RequestBody LineRequest lineRequest)
    {
        if(this.lineService.modifyLine(id, lineRequest) < 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().build();
    }
}
