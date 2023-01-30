package subway.line;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLine(@RequestBody final LineCreateRequest lineCreateRequest) {

        lineService.createLine(lineCreateRequest);
    }

    @GetMapping("/{lineId}")
    public LineResponse getLine(@PathVariable final Long lineId) {

        return lineService.getById(lineId);
    }
}
