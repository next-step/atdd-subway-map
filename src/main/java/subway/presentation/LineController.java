package subway.presentation;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.SectionService;
import subway.dto.LineCreateRequest;
import subway.dto.LineEditRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRegisterRequest;

@RestController
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody @Valid final LineCreateRequest lineCreateRequest) {
        Long lineId = lineService.save(lineCreateRequest);
        return ResponseEntity.created(showLineUriBy(lineId)).build();
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.getList();
        return ResponseEntity.ok()
                .body(lineResponses);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable final Long lineId) {
        LineResponse lineResponse = lineService.getBy(lineId);
        return ResponseEntity.ok()
                .body(lineResponse);
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity editLine(
            @PathVariable final Long lineId,
            @RequestBody final LineEditRequest lineEditRequest
    ) {
        lineService.edit(lineId, lineEditRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity deleteLine(@PathVariable final Long lineId) {
        lineService.delete(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity registerSection(
            @PathVariable final Long lineId,
            @RequestBody @Valid SectionRegisterRequest sectionRegisterRequest
    ) {
        sectionService.registerSection(lineId, sectionRegisterRequest);
        return ResponseEntity.created(showLineUriBy(lineId)).build();
    }

    private static URI showLineUriBy(final Long lineId) {
        return URI.create("/lines/" + lineId);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity deleteSection(
            @PathVariable final Long lineId,
            @RequestParam(name = "stationId") final Long stationId
    ) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
