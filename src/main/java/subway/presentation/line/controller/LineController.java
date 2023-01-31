package subway.presentation.line.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.LineFacade;
import subway.presentation.line.dto.request.LineRequest;
import subway.presentation.line.dto.response.LineResponse;

import java.net.URI;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineFacade lineFacade;

    @PostMapping("")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = lineFacade.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
}