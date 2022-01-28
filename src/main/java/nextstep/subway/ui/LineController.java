package nextstep.subway.ui;

import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.LineCreationResponse;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.applicaion.service.LineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(LineController.LINES)
public class LineController {

    public static final String LINES = "/lines";
    public static final String ID = "/{id}";
    public static final String SECTIONS = "/sections";

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineCreationResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineCreationResponse line = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create(LineController.LINES + "/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {

        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(LineController.ID)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {

        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping(LineController.ID)
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(LineController.ID)
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(LineController.ID + LineController.SECTIONS)
    public ResponseEntity<?> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = lineService.createSection(id, sectionRequest);

        return ResponseEntity
                .created(URI.create(LineController.LINES + "/"+ id + LineController.SECTIONS + "/" + section.getId()))
                .body(section);
    }

    @DeleteMapping(LineController.ID + LineController.SECTIONS)
    public ResponseEntity<Void> deleteSection(@PathVariable(value = "id") Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
