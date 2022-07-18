package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(path = "/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse savedLine = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + savedLine.getId())).body(savedLine);
    }

    @GetMapping(path = "/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping(path = "/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        LineResponse line = lineService.getLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping(path = "/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(lineRequest, id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(path = "/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable long lineId) {
        List<SectionResponse> sections = lineService.getSections(lineId);
        return ResponseEntity.ok().body(sections);
    }

    @PostMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> registerSection(@PathVariable long lineId,
            @RequestBody SectionRequest sectionRequest) {

        SectionResponse section = lineService.registerSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + section.getId())).body(section);
    }

    @DeleteMapping(path = "/lines/{lineId}/sections")
    public ResponseEntity<Void> removeSection(@PathVariable long lineId, @RequestParam long stationId) {
        lineService.removeSection(lineId, stationId);
        return ResponseEntity.ok().body(null);
    }
}
