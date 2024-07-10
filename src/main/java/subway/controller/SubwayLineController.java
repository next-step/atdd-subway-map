package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SubwayLineRequest;
import subway.dto.SubwayLineResponse;
import subway.dto.SubwayLineUpdateRequest;
import subway.service.SubwayLineService;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class SubwayLineController {
    private final SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping
    ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest request) {
        var response = subwayLineService.saveSubwayLine(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<SubwayLineResponse>> showSubwayLines() {
        var response = subwayLineService.findAllSubwayLines();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<SubwayLineResponse> showSubwayLine(@PathVariable Long id) {
        var response = subwayLineService.findSubwayLine(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateSubwayLine(@PathVariable Long id, @RequestBody SubwayLineUpdateRequest request) {
        subwayLineService.updateSubwayLine(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
        subwayLineService.deleteSubwayLine(id);
        return ResponseEntity.noContent().build();
    }


}
