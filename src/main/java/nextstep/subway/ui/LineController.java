package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;
    private final StationService stationService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = lineService.saveLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok(lineService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable("id") Long lineId) {
        return ResponseEntity.ok(lineService.findLine(lineId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineResponse> modifyLine(@PathVariable("id") Long lineId,
                                                   @RequestBody LineRequest request) {
        return ResponseEntity.ok(lineService.updateLine(lineId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable("id") Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> addSection(@RequestBody SectionRequest request,
                                                      @PathVariable("id") Long lineId) {
        lineService.addSection(lineId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable("id") Long lineId, HttpServletRequest request) {
        Station station = stationService.findById(Long.valueOf(request.getParameter("stationId")));

        lineService.deleteSection(lineId, station);
        return ResponseEntity.noContent().build();
    }

}
