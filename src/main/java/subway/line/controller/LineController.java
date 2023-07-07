package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.request.UpdateLineRequestDto;
import subway.line.dto.response.LineResponseDto;
import subway.line.service.LineService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponseDto> createLine(@RequestBody @Valid SaveLineRequestDto lineRequest) {
        LineResponseDto line = lineService.saveLine(lineRequest);

        return ResponseEntity
                .created(URI.create(String.format("/lines/%d", line.getId())))
                .body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponseDto>> showLines() {
        return ResponseEntity.ok(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponseDto> showLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineById(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLineRequestDto lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
