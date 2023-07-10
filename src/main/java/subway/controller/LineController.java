package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.service.LineCompositeService;
import subway.service.SectionCompositeService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineCompositeService lineCompositeService;
    private final SectionCompositeService sectionCompositeService;

    public LineController(LineCompositeService lineCompositeService, SectionCompositeService sectionCompositeService) {
        this.lineCompositeService = lineCompositeService;
        this.sectionCompositeService = sectionCompositeService;
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok()
                             .body(lineCompositeService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        return ResponseEntity.ok()
                             .body(lineCompositeService.findById(id));
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineResponse line = lineCompositeService.saveLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
                             .body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable Long id, @RequestBody LineModifyRequest lineModifyRequest) {
        LineResponse line = lineCompositeService.modifyLine(id, lineModifyRequest);
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCompositeService.deleteLineById(id);
        return ResponseEntity.noContent()
                             .build();
    }
    @GetMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok()
                             .body(sectionCompositeService.findSectionsByLine(lineId));
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable("lineId") Long lineId, @RequestBody SectionSaveRequest sectionSaveRequest) {
        SectionResponse section = sectionCompositeService.saveSection(lineId, sectionSaveRequest);
        return ResponseEntity.created(URI.create("/sections/" + section.getId()))
                             .body(section);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable("lineId") Long lineId, @RequestParam("stationId") Long stationId) {
        sectionCompositeService.deleteSectionByStationId(lineId, stationId);
        return ResponseEntity.noContent()
                             .build();
    }
}
