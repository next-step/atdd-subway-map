package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LineResponse> showLines() {
        return lineService.findAllLines();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LineResponse showLine(@PathVariable Long id) {
        return lineService.findLineById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LineResponse modifyLine(
            @PathVariable Long id,
            @RequestBody LineRequest lineRequest
    ) {
        return lineService.updateLine(id, lineRequest);
    }

}
