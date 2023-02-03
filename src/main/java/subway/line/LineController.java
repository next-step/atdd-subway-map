package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("")
    public ResponseEntity<LineCreateResponse> craeteStation(@RequestBody LineCreateRequest request) {
        LineCreateResponse response = lineService.createStationLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<LineReadListResponse>> readStationLineList() {
        return ResponseEntity
                .ok(lineService.readStationLineList());
    }

    @GetMapping("/{stationLineId}")
    public ResponseEntity<LineReadResponse> readStationLine(@PathVariable Long stationLineId) {
        return ResponseEntity
                .ok(lineService.readStationLine(stationLineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> readLine(@PathVariable Long lineId,
                                                @RequestBody LineUpdateRequest request) {
        lineService.updateStationLine(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteStationLine(lineId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> extendLine(@PathVariable String lineId,
                                                  @RequestBody LineExtendRequest request) {
        LineExtendResponse response = lineService.extendStationLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .build();
    }

}
