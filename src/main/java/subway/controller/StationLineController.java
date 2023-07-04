package subway.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.service.request.StationLineModifyRequest;
import subway.service.request.StationLineRequest;
import subway.service.response.StationLineResponse;
import subway.service.StationLineService;

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

    @DeleteMapping("/lines/{id}")
    ResponseEntity<Object> modifyStationLine(@PathVariable long id) {

        stationLineService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
