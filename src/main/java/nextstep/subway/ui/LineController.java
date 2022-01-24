package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.ui.path.LinePath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(LinePath.ROOT)
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineCreationResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineCreationResponse line = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {

        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(LinePath.ID)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {

        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping(LinePath.ID)
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(LinePath.ID)
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(LinePath.ID + "/sections")
    public ResponseEntity<?> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = sectionService.createSection(id, sectionRequest);

        return ResponseEntity.created(URI.create(LinePath.ROOT + "/"+ id + "/sections/" + section.getId())).body(section);
    }
}
