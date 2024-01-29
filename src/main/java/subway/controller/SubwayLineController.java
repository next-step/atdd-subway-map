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
    public ResponseEntity<SubwayLineResponse> createStation(@RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse subwayLine = subwayLineService.saveSubwayLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLine.getId())).body(subwayLine);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<SubwayLineResponse>> showStations() {
        return ResponseEntity.ok().body(subwayLineService.findAllSubwayLines());
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        subwayLineService.deleteSubwayLineById(id);
        return ResponseEntity.noContent().build();
    }
}
