package subway.line.presentation;

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
import subway.line.application.LineDto;
import subway.line.application.LineService;
import subway.line.application.SectionDto;
import subway.line.application.UpdateLineDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest createLineRequest) {
        LineResponse lineResponse = lineService.createLine(LineDto.from(createLineRequest));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> getLineById(
            @PathVariable long lineId
    ) {
        LineResponse lineResponse = lineService.findLineById(lineId);
        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLineById(
            @PathVariable long lineId,
            @RequestBody UpdateLineRequest updateLineRequest
    ) {
        LineResponse lineResponse = lineService.updateLineById(lineId, UpdateLineDto.from(updateLineRequest));
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLineById(
            @PathVariable long lineId
    ) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> addSection(
            @PathVariable long lineId,
            @RequestBody AddSectionRequest addSectionRequest
    ) {
        LineResponse lineResponse = lineService.addSection(lineId, SectionDto.from(addSectionRequest));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam("stationId") long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
