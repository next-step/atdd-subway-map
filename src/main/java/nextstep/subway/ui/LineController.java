package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    public LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStationLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineService.saveStationLine(lineRequest);
        return ResponseEntity.created(URI.create("/line/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllStationsLines());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findStationLine(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateStationLine(@RequestBody LineUpdateRequest lineUpdateRequest,
                                                  @PathVariable Long id) {
        lineService.updateStationLine(lineUpdateRequest, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
        lineService.deleteStationLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> createStationSection(@PathVariable Long id,
                                                             @RequestBody SectionRequest sectionRequest) {
        LineResponse lineResponse = lineService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/section/" + lineResponse.getId())).body(lineResponse);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteStationSection(@PathVariable Long lineId,
                                                     @RequestParam Long downStationId) {
        lineService.deleteSection(lineId, downStationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/lines/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SectionResponse>> getSections() {
        List<SectionResponse> responses = lineService.getSections();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(value = "/lines/{id}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable Long id) {
        List<SectionResponse> responses = lineService.getSections(id);
        return ResponseEntity.ok().body(responses);
    }

}
