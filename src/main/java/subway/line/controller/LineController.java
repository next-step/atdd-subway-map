package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.domain.Line;
import subway.line.LineService;
import subway.line.packet.LineRequest;
import subway.line.packet.LineResponse;
import subway.line.packet.LineUpdateRequest;
import subway.line.service.LineService;

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

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines(){
        List<Line> stationLines = lineService.getStationLines();
        List<LineResponse> response = stationLines.stream().map(LineResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineRequest){
        lineService.updateStationLine(id, lineRequest.getName(), lineRequest.getColor());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id){
        lineService.deleteStationLine(id);
        return ResponseEntity.noContent().build();
    }
}
