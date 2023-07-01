package subway.line;

import static org.springframework.http.ResponseEntity.status;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineCreateResponse> createLines(@RequestBody LineCreateRequest request) {
        return status(HttpStatus.CREATED).body(lineService.createStation(request));
    }
}
