package subway.line;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.line.request.LineRequest;
import subway.line.response.LineResponse;

@RequiredArgsConstructor
@RestController
public class LineController {

    private final LineService lineService;

    @PostMapping(
        value = "/lines",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = this.lineService.create(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }
}