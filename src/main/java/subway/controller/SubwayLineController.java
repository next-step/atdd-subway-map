package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.request.StationRequest;
import subway.domain.request.SubwayLineRequest;
import subway.domain.response.StationResponse;
import subway.domain.response.SubwayLineResponse;
import subway.service.StationService;
import subway.service.SubwayLineService;

import java.net.URI;
import java.util.List;

@RestController
public class SubwayLineController {
    private SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse subwayLine = subwayLineService.saveSubwayLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLine.getId())).body(subwayLine);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<SubwayLineResponse>> showSubwayLines() {
        return ResponseEntity.ok().body(subwayLineService.findAllSubwayLines());
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<SubwayLineResponse> showSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findSubwayLineById(id));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<SubwayLineResponse> updateSubwayLine(@PathVariable Long id, @RequestBody SubwayLineRequest subwayLineRequest) {
        return ResponseEntity.ok().body(subwayLineService.updateSubwayLine(id, subwayLineRequest));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLineById(id);
        return ResponseEntity.noContent().build();
    }
}
