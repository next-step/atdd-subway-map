package nextstep.subway.ui;

import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/station/line")
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping
    public ResponseEntity<StationLineResponse> create(@RequestBody StationLineRequest request) {
        StationLineResponse response = stationLineService.save(request.toEntity());
        return ResponseEntity.created(URI.create("/station/line/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StationLineResponse>> findAll() {
        List<StationLineResponse> response = stationLineService.findAllStationLines();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationLineResponse> findById(@PathVariable Long id) {
        return  ResponseEntity.ok(stationLineService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StationLineResponse> patchLine(
            @PathVariable Long id, @RequestBody StationLineRequest.PatchRequest patchRequest) {
        return ResponseEntity.ok(stationLineService.patch(id, patchRequest.toEntity()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        stationLineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

}
