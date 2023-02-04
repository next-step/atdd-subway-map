package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.dto.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineDto> createLine(@RequestBody LineCreateDto lineCreateDto) {
        LineDto line = lineService.saveLine(lineCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineDto>> readLines() {
        List<LineDto> line = lineService.readLines();
        return ResponseEntity.ok().body(line);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineDto> readLine(@PathVariable Long id) {
        LineDto line = lineService.readLine(id);
        return ResponseEntity.ok(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineModifyDto lineModifyDto) {
        lineService.modifyLine(id, lineModifyDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineDto> addSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest sectionCreateRequest) {
        var createDto = new SectionAddDto(lineId, sectionCreateRequest.getUpStationId(), sectionCreateRequest.getDownStationId(), sectionCreateRequest.getDistance());
        lineService.addSection(createDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineDto> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        var deleteDto = new SectionDeleteDto(lineId, stationId);
        lineService.deleteSection(deleteDto);
        return ResponseEntity.noContent().build();
    }
}
