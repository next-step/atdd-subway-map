package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLineAll() {
        List<LineResponse> line = lineService.findAllLine();
        return ResponseEntity.ok().body(line);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long lineId) {
        LineResponse line = lineService.findLineById(lineId);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/{lineId}")
    public void modifyLineInfo(@PathVariable Long lineId
            , @RequestBody LineRequest lineRequest) {
        lineService.modifyLine(lineId, lineRequest);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        LineResponse line = lineService.addSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @DeleteMapping(value = "/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam(value = "stationId") Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sections")
    public ResponseEntity<List<SectionResponse>> getLineSection() {
        List<SectionResponse> line = lineService.findAllSection();
        return ResponseEntity.ok().body(line);
    }

}
