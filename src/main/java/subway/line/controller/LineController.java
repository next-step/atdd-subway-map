package subway.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        LineResponse lineResponse = lineService.saveStationLine(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.toEntity());
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id){
        LineResponse response = lineService.getStationLine(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines(){
        List<LineResponse> response = lineService.getStationLines();
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
