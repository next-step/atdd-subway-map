package subway.station.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.station.domain.section.Section;
import subway.station.service.LineService;
import subway.station.service.SectionService;
import subway.station.service.dto.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineSaveResponse> saveLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineSaveResponse line = lineService.save(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineFindAllResponse>> viewLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineFindByLineResponse> findLineById(@PathVariable Long id) {
        LineFindByLineResponse line = lineService.findById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineUpdateResponse> updateLine(@PathVariable Long id, @RequestBody LineUpdateResponse updateLineRequest) {
        LineUpdateResponse line = lineService.update(id, updateLineRequest.getName(), updateLineRequest.getColor());
        return ResponseEntity.ok().body(line);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<SectionSaveResponse> saveSection(@PathVariable Long id, @RequestBody SectionSaveRequest sectionSaveRequest) {
        SectionSaveResponse section = sectionService.save(id, sectionSaveRequest);
        if(Objects.isNull(section)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections/" + section.getId())).body(section);
    }

    @DeleteMapping("/lines/{lineId}/sections/{stationId}")
    public ResponseEntity<SectionSaveResponse> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        Boolean result = sectionService.delete(lineId, stationId);
        if(result.equals(false)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
