package subway.line;

import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    @PostMapping("/lines")
    public ResponseEntity<LineCreateResponse> createLines(@RequestBody LineCreateRequest request) {
        return status(HttpStatus.CREATED).body(new LineCreateResponse());
    }
}
