package nextstep.subway.ui;

import javassist.NotFoundException;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.SectionRequest;
import nextstep.subway.exception.NotFoundStationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        try {
            LineResponse line = lineService.saveLine(lineRequest);
            return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok(lineService.getLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<?> getLine(
            @PathVariable long lineId
    ) {
        return ResponseEntity.ok(lineService.getLine(lineId));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<?> getLine(
            @PathVariable long lineId,
            @RequestBody LineRequest lineRequest
    ) {
        lineService.updateLine(lineId, lineRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<HttpStatus> deleteLine(
            @PathVariable long lineId
    ) {
        lineService.deleteLine(lineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> addSectionToLine(
            @PathVariable long lineId,
            @RequestBody SectionRequest sectionRequest
            ) {
        return new ResponseEntity<>(lineService.addSectionToLine(lineId, sectionRequest), HttpStatus.CREATED);
    }

}
