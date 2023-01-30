package subway.line;

import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.common.ErrorResponse;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
        LineResponse response = lineService.saveLine(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLineList() {
        List<LineResponse> response = lineService.findAllLines();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable(name = "id") Long id) {
        LineResponse response = lineService.findLineById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable(name = "id") Long id, @RequestBody UpdateLineRequest request) {
        lineService.updateLineById(id, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable(name = "id") Long id) {
        lineService.deleteLineById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
