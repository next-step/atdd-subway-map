package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines(){
        return ResponseEntity.ok().body(lineService.findAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id){
        return ResponseEntity.ok().body(lineService.findStationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity modifyLine(@RequestBody LineRequest lineRequest, @PathVariable Long id){
        lineService.modifyLine(lineRequest, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id){
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> registerLineSection(@PathVariable Long id, @RequestBody LineRequest lineRequest){
        LineResponse response = lineService.registerLineSection(id, lineRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteLineSection(@PathVariable Long lineId, @RequestParam("stationId") Long stationId){
        LineResponse response = lineService.deleteLineSection(lineId, stationId);
        return ResponseEntity.ok().body(response);
    }

}
