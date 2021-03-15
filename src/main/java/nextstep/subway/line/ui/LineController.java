package nextstep.subway.line.ui;

import nextstep.subway.common.error.ErrorInformation;
import nextstep.subway.line.DuplicatedLineNameException;
import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }


    @GetMapping("/lines")
    public ResponseEntity findAllLines(){
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity.ok().body(lineResponses);
    }

    @PostMapping("/lines")
    public ResponseEntity createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity findLineById(@PathVariable Long id) {
        LineResponse line = lineService.findLineById(id);
        return ResponseEntity.ok().body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity addSections(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity removeSection(@PathVariable Long lineId, @RequestParam Long stationId){
        lineService.removeSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity notFoundLineExceptionHandler(NotFoundLineException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicatedLineNameException.class)
    public ResponseEntity duplicatedLineNameException(DuplicatedLineNameException e){
        ErrorInformation errorInformation = new ErrorInformation(e.getMessage());
        return ResponseEntity.badRequest().body(errorInformation);
    }
}
