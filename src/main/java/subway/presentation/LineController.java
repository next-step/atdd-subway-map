package subway.presentation;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineEditRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody @Valid final LineRequest lineRequest) {
        Long lineId = lineService.save(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.getList();

        return ResponseEntity.ok()
                .body(lineResponses);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.get(lineId);

        return ResponseEntity.ok()
                .body(lineResponse);
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity editLine(
            @PathVariable Long lineId,
            @RequestBody LineEditRequest lineEditRequest
    ) {
        lineService.edit(lineId, lineEditRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable Long lineId) {
        lineService.delete(lineId);

        return ResponseEntity.noContent().build();
    }
}
