package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id){
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id){
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest){
        return ResponseEntity.ok().body(lineService.updateLine(id,lineRequest));
    }

    @GetMapping("/lines/{id}/sections")
    public ResponseEntity<List<StationResponse>> showAllStationsWithLineId(@PathVariable Long id) {
        LineResponse lineResponse = lineService.findLine(id);
        return ResponseEntity.ok().body(lineResponse.getStations());
    }

    @GetMapping("/lines/{id}/sections/last-section")
    public ResponseEntity<SectionResponse> showLastSection(@PathVariable Long id) {
        SectionResponse sectionResponse = lineService.findLastSection(id);
        return ResponseEntity.ok().body(sectionResponse);
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<LineResponse> createSections(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        LineResponse lineResponse = lineService.addSection(id,sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    //    /lines/{lineId}/sections?stationId={stationId}
    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity removeSection(@PathVariable Long lineId,@RequestParam Long stationId){
        lineService.deleteSection(lineId,stationId);
        return ResponseEntity.noContent().build();
    }


}
