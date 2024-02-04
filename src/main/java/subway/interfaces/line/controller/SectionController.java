package subway.interfaces.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.line.LineCommand;
import subway.domain.line.service.LineService;
import subway.domain.line.service.SectionService;
import subway.interfaces.line.dto.LinePatchRequest;
import subway.interfaces.line.dto.LineRequest;
import subway.interfaces.line.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines/{line-id}/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createSection(@PathVariable("line-id") Long lineId, @RequestBody LineRequest.Section request) {
        LineResponse line = sectionService.saveSection(LineCommand.SectionAddCommand.of(lineId, request));
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @DeleteMapping
    public ResponseEntity<LineResponse> deleteSection(@PathVariable("line-id") Long lineId, @RequestParam("stationId") Long stationId) {
        LineResponse line = sectionService.removeSection(LineCommand.SectionDeleteCommand.of(lineId, stationId));
        return ResponseEntity.noContent().build();
    }
}
