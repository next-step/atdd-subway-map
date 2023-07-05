package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.request.SubwayLineCreateRequest;
import subway.controller.request.SubwayLineModifyRequest;
import subway.controller.request.SubwayLineSectionAddRequest;
import subway.controller.resonse.SubwayLineResponse;
import subway.controller.resonse.SubwayLineSectionResponse;
import subway.service.SubwayLineService;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class StationLineController {

    private final SubwayLineService subwayLineService;

    public StationLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping
    public ResponseEntity<SubwayLineResponse> createStationLine(@RequestBody SubwayLineCreateRequest subwayLineCreateRequest) {
        SubwayLineResponse subwayLineResponse = subwayLineService.saveStationLine(subwayLineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLineResponse.getId())).body(subwayLineResponse);
    }

    @GetMapping
    public ResponseEntity<List<SubwayLineResponse>> showStationLines() {
        return ResponseEntity.ok().body(subwayLineService.findAllSubwayLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubwayLineResponse> showStationLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findSubwayLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyStationLine(@PathVariable Long id, @RequestBody SubwayLineModifyRequest subwayLineModifyRequest) {
        subwayLineService.modifySubwayLine(id, subwayLineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SubwayLineSectionResponse> addStationLineSection(@PathVariable Long id, @RequestBody SubwayLineSectionAddRequest subwayLineCreateRequest) {
        SubwayLineSectionResponse subwayLineSectionResponse = subwayLineService.addStationSection(id, subwayLineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections" + subwayLineSectionResponse.getId())).body(subwayLineSectionResponse);
    }

}
