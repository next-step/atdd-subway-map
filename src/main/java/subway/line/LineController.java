package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LineController {


    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine() {
        Long lineId = 1L;
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
