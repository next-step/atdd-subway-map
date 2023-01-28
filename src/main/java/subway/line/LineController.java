package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> save(@RequestBody LineCreateRequest lineCreateRequest) {
        LineResponse lineResponse = lineService.save(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping("/lines")
    public List<LineResponse> findAll() {
        return lineService.findAll();
    }

    @GetMapping("/lines/{id}")
    public LineResponse findById(@PathVariable Long id) {
        return lineService.findById(id);
    }

    @PutMapping("/lines/{id}")
    public void update(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
        request.setLineId(id);
        lineService.update(request);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
