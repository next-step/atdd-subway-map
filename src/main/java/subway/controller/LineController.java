package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.packet.LineRequest;
import subway.packet.LineResponse;
import subway.service.LineService;

import java.net.URI;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest){
        Line line = lineService.saveStationLine(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.toEntity());
        LineResponse lineResponse = LineResponse.fromEntity(line);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(lineResponse);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id){
        Line line = lineService.getStationLine(id);
        LineResponse lineResponse = LineResponse.fromEntity(line);
        return ResponseEntity.ok(lineResponse);
    }
}
