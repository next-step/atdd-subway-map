package nextstep.subway.applicaion.line.ui;

import nextstep.subway.applicaion.line.LineService;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    // 목록
    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    // 등록
    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStation(@RequestBody LineRequest LineRequest) {
        return ResponseEntity.ok().body(lineService.saveLine(LineRequest));
    }

    // 조회
    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLineById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findById(id));
    }

    // 수정
    @PutMapping("/lines")
    public ResponseEntity<LineResponse> updateStationById(@RequestBody LineRequest LineRequest) {
        LineResponse line = lineService.updateLine(LineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    // 삭제
    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
