package subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineService;
import subway.line.application.dto.request.LineCreateRequest;
import subway.line.application.dto.request.LineUpdateRequest;
import subway.line.application.dto.response.LineResponse;
import subway.line.domain.Line;
import subway.section.application.SectionService;
import subway.section.application.dto.request.SectionCreateRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    ResponseEntity<LineResponse> createLine(@RequestBody final LineCreateRequest lineCreateRequest) {
        Long lineId = lineService.saveLine(lineCreateRequest);
        Line findLine = lineService.findLineById(lineId);

        return ResponseEntity.created(URI.create("/lines/" + lineId))
                .body(LineResponse.from(findLine));
    }

    @GetMapping
    ResponseEntity<List<LineResponse>> showLines() {
        List<Line> findLines = lineService.findAllLines();

        return ResponseEntity.ok()
                .body(LineResponse.fromList(findLines));
    }

    @GetMapping("/{lineId}")
    ResponseEntity<LineResponse> showLine(@PathVariable final Long lineId) {
        Line findLine = lineService.findLineById(lineId);

        return ResponseEntity.ok()
                .body(LineResponse.from(findLine));
    }

    @PutMapping("/{lineId}")
    ResponseEntity<Void> updateLine(@PathVariable final Long lineId,
                                    @RequestBody final LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {
        lineService.deleteLine(lineId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    ResponseEntity<Void> createSection(@PathVariable final Long lineId,
                                       @RequestBody final SectionCreateRequest sectionCreateRequest) {
        Line findLine = lineService.findLineById(lineId);
        sectionService.saveSection(findLine, sectionCreateRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
