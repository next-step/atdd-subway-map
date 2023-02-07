package subway.line;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.*;
import java.util.*;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineCreateRequest lineCreateRequest) {

        final var response = lineService.createLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + response.getId()))
                .body(response);

    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable final Long lineId) {

        return ResponseEntity.ok(lineService.getLine(lineId));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {

        return ResponseEntity.ok(lineService.getAll());
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> editLine(
            @PathVariable final Long lineId,
            @RequestBody final LineEditRequest lineEditRequest
    ) {

        lineService.editLine(lineId, lineEditRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {

        lineService.deleteById(lineId);
        return ResponseEntity.noContent().build();
    }

}
