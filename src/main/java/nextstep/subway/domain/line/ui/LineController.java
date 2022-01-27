package nextstep.subway.domain.line.ui;

import nextstep.subway.domain.line.LineService;
import nextstep.subway.domain.line.dto.LineDetailResponse;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.section.dto.SectionDetailResponse;
import nextstep.subway.domain.section.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineDetailResponse>> getLineList() {
        return ResponseEntity.ok(lineService.getLineList());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineDetailResponse> getLine(@PathVariable(name = "lineId") Long id) {
        LineDetailResponse lineDetailResponse = lineService.getLine(id);

        if (lineDetailResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lineDetailResponse);
    }

    @PatchMapping("/{lineId}")
    public ResponseEntity patchLine(@PathVariable(name = "lineId") Long id,
                                    @RequestBody LineRequest lineRequest) {
        lineService.patchLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity deleteLine(@PathVariable(name = "lineId") Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable(value = "lineId") Long lineId,
                                                         @RequestBody LineRequest request) {
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/"))
                .body(lineService
                        .createSection(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance()));
    }

    @GetMapping("/{lineId}/sections")
    public ResponseEntity<List<SectionDetailResponse>> getSections(@PathVariable(value = "lineId") Long id) {
        return ResponseEntity.ok(lineService.getSections(id));
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity deleteSection(@PathVariable(value = "lineId") Long lineId,
                                        @RequestParam(name = "stationId") Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
