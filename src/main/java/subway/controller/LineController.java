package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.*;
import subway.service.LineService;
import subway.service.dto.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponseBody> createLine(@RequestBody LineCreateRequestBody lineCreateRequestBody) {
        LineDto line = lineService.saveLine(new SaveLineDto(
                lineCreateRequestBody.getName(),
                lineCreateRequestBody.getColor(),
                lineCreateRequestBody.getUpStationId(),
                lineCreateRequestBody.getDownStationId(),
                lineCreateRequestBody.getDistance()
        ));
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(LineResponseBody.create(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponseBody>> showLines() {
        List<LineDto> lines = lineService.findAllLines();
        return ResponseEntity.ok().body(LineResponseBody.create(lines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponseBody> showLine(@PathVariable Long id) {
        LineDto line = lineService.getLineByIdOrFail(id);
        return ResponseEntity.ok().body(LineResponseBody.create(line));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long id,
            @RequestBody LineUpdateRequestBody lineUpdateRequestBody
    ) {
        lineService.updateLine(new UpdateLineDto(
                id,
                lineUpdateRequestBody.getName(),
                lineUpdateRequestBody.getColor()
        ));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponseBody> createLineSection(
            @PathVariable Long id,
            @RequestBody SectionCreateRequestBody sectionCreateRequestBody
    ) {
        lineService.saveLineSection(new SaveLineSectionDto(
                id,
                sectionCreateRequestBody.getUpStationId(),
                sectionCreateRequestBody.getDownStationId(),
                sectionCreateRequestBody.getDistance()
        ));
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    private StationResponseBody toStationResponseBody(StationDto stationDto) {
        return new StationResponseBody(stationDto.getId(), stationDto.getName());
    }
}
