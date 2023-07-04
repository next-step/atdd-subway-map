package subway.stationline;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StationLineController {

    private final StationLineService stationLineService;

    public StationLineController(final StationLineService stationLineService) {
        this.stationLineService = stationLineService;
    }

    @PostMapping("/lines")
    ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest request) {

        StationLineResponse response = stationLineService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping("/lines")
    ResponseEntity<List<StationLineResponse>> getStationLineList() {

        return ResponseEntity.ok(stationLineService.findAllStationLines());
    }

    @GetMapping("/lines/{id}")
    ResponseEntity<StationLineResponse> getStationLine(@PathVariable long id) {

        return ResponseEntity.ok(stationLineService.findById(id));
    }

    @PutMapping("/lines/{id}")
    ResponseEntity<Object> modifyStationLine(
        @PathVariable long id,
        @RequestBody StationLineModifyRequest request
    ) {

        stationLineService.modify(id, request);
        return ResponseEntity.ok().build();
    }
}
