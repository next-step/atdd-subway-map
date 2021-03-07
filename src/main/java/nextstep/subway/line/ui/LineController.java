package nextstep.subway.line.ui;

import nextstep.subway.exceptions.AlreadyExistsEntityException;
import nextstep.subway.exceptions.NotEqualsStationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity showLines() {
        List<LineResponse> lines = lineService.readLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping("/{id}")
    public ResponseEntity showLine(@PathVariable Long id) {
        LineResponse line = lineService.readLine(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Line line = lineService.addLineStation(lineId, sectionRequest);
        Section lastSection = line.getLastSection();
        SectionResponse sectionResponse = SectionResponse.of(lastSection);

        return ResponseEntity.created(URI.create(String.format("/lines/%d/sections/%d", lineId, lastSection.getId()))).body(sectionResponse);
    }

    @ExceptionHandler(AlreadyExistsEntityException.class)
    public ResponseEntity alreadyExistsEntityException(AlreadyExistsEntityException e) {
        return ResponseEntity.badRequest().body(createMessageMap("cause", e.getMessage()));
    }

    @ExceptionHandler(NotEqualsStationException.class)
    public ResponseEntity notEqualsStationException(NotEqualsStationException e) {
        return ResponseEntity.badRequest().body(createMessageMap("cause", e.getMessage()));
    }

    private Map createMessageMap(String key, String value) {
        return new HashMap<String, String>(){{put(key, value);}};
    }
}
