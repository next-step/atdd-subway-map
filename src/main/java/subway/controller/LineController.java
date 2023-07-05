package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineDto;
import subway.service.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.net.URI;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
            LineDto lineDto = lineService.saveLine(lineRequest.toDto());
            return ResponseEntity.created(URI.create("/lines/" + lineDto.getId())).body(LineResponse.from(lineDto));
    }
}
