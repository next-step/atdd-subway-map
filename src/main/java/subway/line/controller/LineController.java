package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.service.dto.LineCreateRequest;
import subway.line.service.dto.LineResponse;
import subway.line.service.LineService;

import java.net.URI;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineCreateRequest createRequest) {
        final LineResponse lineResponse = lineService.saveLine(createRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }
}
