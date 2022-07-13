package nextstep.subway.ui;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionCreationRequest;
import nextstep.subway.applicaion.line.LineQueryService;
import nextstep.subway.applicaion.line.LineService;
import nextstep.subway.applicaion.section.SectionService;
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

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;
    private final LineQueryService lineQueryService;
    private final SectionService sectionService;

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        var lines = lineQueryService.getAllLines();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLineById(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok(lineQueryService.getLineById(lineId));
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreationRequest request) {
        var line = lineService.create(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> modifyLine(
            @PathVariable("lineId") Long lineId,
            @RequestBody LineModificationRequest request
    ) {
        lineService.modify(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable("lineId") Long lineId) {
        lineService.remove(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable("lineId") Long lineId,
            @RequestBody SectionCreationRequest request
    ) {
        sectionService.addSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "?stationId=" + request.getDownStationId())).build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(
            @PathVariable("lineId") Long lineId,
            @RequestParam("stationId") Long stationId
    ) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
