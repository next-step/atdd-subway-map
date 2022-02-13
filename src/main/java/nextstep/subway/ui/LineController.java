package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.LineService;
import nextstep.subway.application.SectionService;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineResponseBy(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id,
                                           @RequestBody LineRequest lineRequest) {
        lineService.updateBy(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteBy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{lineId}/sections")
    public ResponseEntity<SectionResponse> addSection(@PathVariable Long lineId,
                                                      @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = sectionService.addSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + section.getId())).body(section);
    }


    @DeleteMapping(value = "/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable("id") Long lineId, HttpServletRequest request) {
        sectionService.deleteBy(lineId, Long.valueOf(request.getParameter("stationId")));
        return ResponseEntity.noContent().build();
    }
}
