package nextstep.subway.ui;

import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.LineCreationResponse;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.applicaion.service.LineService;
import nextstep.subway.applicaion.service.SectionService;
import nextstep.subway.ui.path.SubwayPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(SubwayPath.LINES)
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

    @GetMapping(SubwayPath.ID)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {

        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping(SubwayPath.ID)
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(SubwayPath.ID)
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLine(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(SubwayPath.ID + SubwayPath.SECTIONS)
    public ResponseEntity<?> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        SectionResponse section = sectionService.createSection(id, sectionRequest);

        return ResponseEntity
                .created(URI.create(SubwayPath.LINES + "/"+ id + SubwayPath.SECTIONS + "/" + section.getId()))
                .body(section);
    }

    @DeleteMapping(SubwayPath.ID + SubwayPath.SECTIONS)
    public ResponseEntity<Void> deleteSection(@PathVariable(value = "id") Long lineId, @RequestParam Long stationId) {
        sectionService.deleteSection(lineId, stationId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
