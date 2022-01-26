package nextstep.subway.line.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.dto.LineResponse;
import nextstep.subway.line.domain.dto.LineWithStationResponse;
import nextstep.subway.line.domain.dto.SectionRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/lines")
public class LineController {
    private static final String SECTION_URL_PATTERN = "/lines/%d/sections/%d";

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineWithStationResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineWithStationResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineResponse> responses = lineService.findAllLines();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("{id}")
    public ResponseEntity<LineWithStationResponse> getLine(@PathVariable("id") long id) {
        LineWithStationResponse responses = lineService.findById(id);
        return ResponseEntity.ok().body(responses);
    }

    @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> editLine(@PathVariable("id") long id, @RequestBody LineRequest lineRequest) {
        lineService.edit(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable("id") long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "{lineId}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addSection(@PathVariable("lineId") final Long lineId,
        @RequestBody SectionRequest request) {
        Long createSectionId = lineService.addSection(lineId, request);
        String location = String.format(SECTION_URL_PATTERN, lineId, createSectionId);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @DeleteMapping(path = "{lineId}/sections/{sectionId}")
    public ResponseEntity<Void> deleteSection(
        @PathVariable("lineId") final Long lineId, @PathVariable("sectionId") final Long sectionId) {
        lineService.deleteSection(lineId, sectionId);

        return ResponseEntity.noContent().build();
    }
}
