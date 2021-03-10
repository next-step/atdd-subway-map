package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.line.exception.InvalidDownStationException;
import nextstep.subway.line.exception.InvalidUpStationException;
import nextstep.subway.line.exception.OnlyOneSectionRemainingException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody final LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> getLine(@PathVariable final Long id) {
        return ResponseEntity.ok().body(lineService.getLine(id));
    }

    @PutMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineResponse> deleteLine(@PathVariable final Long id) {
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity createSection(@PathVariable final Long lineId, @RequestBody final SectionRequest sectionRequest) {
        SectionResponse section = lineService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/" + section.getId())).body(section);
    }

    @DeleteMapping(value = "/lines/{lineId}/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteSection(@PathVariable final Long lineId, @RequestParam final Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler({InvalidUpStationException.class,InvalidDownStationException.class,OnlyOneSectionRemainingException.class})
    private ResponseEntity InvalidUpStationException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }

/*    @ExceptionHandler(InvalidDownStationException.class)
    private ResponseEntity InvalidDownStationException(InvalidDownStationException invalidDownStationException){
        return ResponseEntity.badRequest().body(invalidDownStationException.getMessage());
    }

    @ExceptionHandler(OnlyOneSectionRemainingException.class)
    private ResponseEntity OnlyOneSectionRemainingException(InvalidDownStationException invalidDownStationException){
        return ResponseEntity.badRequest().body(invalidDownStationException.getMessage());
    }*/

}
