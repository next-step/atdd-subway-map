package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.request.SubwayLineCreateRequest;
import subway.controller.request.SubwayLineModifyRequest;
import subway.controller.resonse.SubwayLineResponse;
import subway.service.SubwayLineService;

import java.net.URI;
import java.util.List;

@RestController
public class StationLineController {

    private final SubwayLineService subwayLineService;

    public StationLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> createStationLine(@RequestBody SubwayLineCreateRequest subwayLineCreateRequest) {
        SubwayLineResponse subwayLineResponse = subwayLineService.saveStationLine(subwayLineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLineResponse.getId())).body(subwayLineResponse);
    }


    @GetMapping("/lines")
    public ResponseEntity<List<SubwayLineResponse>> showStationLines() {
        return ResponseEntity.ok().body(subwayLineService.findAllSubwayLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<SubwayLineResponse> showStationLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findSubwayLine(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> modifyStationLine(@PathVariable Long id, @RequestBody SubwayLineModifyRequest subwayLineModifyRequest) {
        subwayLineService.modifySubwayLine(id, subwayLineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLineById(id);
        return ResponseEntity.noContent().build();
    }

}
