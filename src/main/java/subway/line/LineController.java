package subway.line;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/lines")
public class LineController {
    private final LineService lineService;

    @PostMapping
    ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        Line line = lineService.saveLine(request);

        LineResponse lineResponse = LineResponse.of(line);

        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        Optional<LineResponse> lineResponse = lineService.findById(id);

        return lineResponse.map(ResponseEntity.ok()::body)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<?> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineModifyRequest) {
        lineService.modifyLine(id, lineModifyRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> modifyLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return ResponseEntity.noContent().build();
    }


}
