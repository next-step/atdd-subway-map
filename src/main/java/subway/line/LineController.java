package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.commons.HttpException;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineFacade lineFacade;

    public LineController(LineFacade lineFacade) {
        this.lineFacade = lineFacade;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse lineResponse = lineFacade.createLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/"+ lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> readLines() {
        List<LineResponse> lineResponses = lineFacade.readLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> readLine(@PathVariable Long id) {
        LineResponse lineResponse = lineFacade.readLine(id);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PatchMapping("/lines/{id}")
    public ResponseEntity<?> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest)
            throws HttpException {
        LineUpdateDTO dto = new LineUpdateDTO(id, lineUpdateRequest);
        lineFacade.updateLine(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<?> deleteLine(@PathVariable Long id) {
        lineFacade.deleteLine(id);
        return ResponseEntity.noContent().build();
    }
}
