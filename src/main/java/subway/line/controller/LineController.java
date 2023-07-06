package subway.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.request.SaveLineRequestDto;
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
        return ResponseEntity.ok()
                .body(lineService.findAllLines());
    }

}
