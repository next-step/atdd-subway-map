package subway.line;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLines(@RequestBody LineCreateRequest request) {
        return status(HttpStatus.CREATED).body(lineService.createStation(request));
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.getLine(id));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        return ResponseEntity.ok(lineService.getList());
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> modifyLine(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
