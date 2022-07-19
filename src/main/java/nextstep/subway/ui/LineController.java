package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.exception.AlreadyRegisteredException;
import nextstep.subway.exception.NotDownStationException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.OnlyOneSectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.createLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> getAllLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.modifyLineById(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        lineService.createSection(id, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        lineService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findSectionsById(id));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFoundExceptionHandler() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ NotDownStationException.class, AlreadyRegisteredException.class, OnlyOneSectionException.class })
    public ResponseEntity<Void> exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }
}
