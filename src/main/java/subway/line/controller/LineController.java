package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionCreateRequest;
import subway.line.service.LineSectionService;
import subway.line.service.LineService;
import subway.line.service.LineStationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;
    private final LineStationService lineStationService;
    private final LineSectionService lineSectionService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineRequest) {
        LineResponse line = lineStationService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> retrieveLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> retrieveLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id,
                                           @RequestBody LineModifyRequest request) {
        lineService.updateLine(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> appendSection(@PathVariable(name = "id") Long lineId, @RequestBody SectionCreateRequest request) {
        // TODO: LineController.appendSection()
        // 노선을 찾아서
        // 노선에 섹션 추가하기
        lineSectionService.appendSection(lineId, request);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/{id}/sections")
//    public ResponseEntity<Void> deleteSection(@PathVariable(name = "id") Long lineId, @RequestParam(name = "stationId") Long stationId) {
//        // TODO: LineController.deleteSection()
//        return ResponseEntity.noContent().build();
//    }


}
