package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> readLines() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(lineService.getAllLines());
    }

    @GetMapping(value = "/{lineId}")
    public ResponseEntity<LineResponse> readLine(@PathVariable Long lineId){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(lineService.getLine(lineId));
    }


    @PutMapping(value = "/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId,
                                                   LineRequest lineRequest){

        lineService.modifyLine(lineId, lineRequest);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping(value = "/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId){
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent()
                .build();
    }


}
