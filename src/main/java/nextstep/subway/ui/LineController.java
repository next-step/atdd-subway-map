package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.exception.InvalidMatchEndStationException;
import nextstep.subway.domain.exception.NotFoundLineException;
import nextstep.subway.domain.exception.NotFoundStationException;
import nextstep.subway.domain.exception.StationAlreadyExistsException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody @Validated LineRequest lineRequest) {
        LineResponse line = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLineResponse());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineResponse(id));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody @Validated LineModifyRequest lineModifyRequest) {
        lineService.update(id, lineModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody @Validated SectionRequest sectionRequest) {
        try {
            LineResponse line = lineService.addSection(id, sectionRequest);
            return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
        } catch (InvalidMatchEndStationException | StationAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (NotFoundLineException | NotFoundStationException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
