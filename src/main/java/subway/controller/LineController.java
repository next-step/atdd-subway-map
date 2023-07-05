package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineDto;
import subway.dto.ModifyLineRequest;
import subway.service.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineDto> lineDtos = lineService.getLines();
        List<LineResponse> responses = lineDtos.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        LineDto lineDto = lineService.getLine(id);
        return ResponseEntity.status(HttpStatus.OK).body(LineResponse.from(lineDto));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity substituteLine(@RequestBody ModifyLineRequest modifyLineRequest, @PathVariable long id) {
        lineService.updateLine(id, modifyLineRequest.toDto());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable long id) {
        lineService.deleteLine(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
