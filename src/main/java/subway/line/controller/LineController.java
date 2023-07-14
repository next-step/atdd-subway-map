package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineDto;
import subway.line.dto.ModifyLineRequest;
import subway.line.service.LineService;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionRequest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineDto lineDto = lineService.saveLine(lineRequest.toDto());
        return ResponseEntity.created(URI.create("/lines/" + lineDto.getId())).body(LineResponse.from(lineDto));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineDto> lineDtos = lineService.getLines();
        List<LineResponse> responses = lineDtos.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
        LineDto lineDto = lineService.getLine(id);
        return ResponseEntity.status(HttpStatus.OK).body(LineResponse.from(lineDto));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity substituteLine(@RequestBody ModifyLineRequest modifyLineRequest, @PathVariable long id) {
        lineService.updateLine(id, modifyLineRequest.toDto());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable long id) {
        lineService.deleteLine(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity enrollSection(@PathVariable long id, @RequestBody SectionRequest sectionRequest) {
        lineService.enroll(id, sectionRequest.toDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity deleteSection(@PathVariable long lineId, @RequestParam(value = "stationId") long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
