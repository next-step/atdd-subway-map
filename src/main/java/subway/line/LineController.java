package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.entity.Line;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> save(@RequestBody LineRequest lineRequest) {
        Line saved = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + saved.getId())).body(LineResponse.from(saved));
    }

    @GetMapping("/lines")
    public List<LineResponse> findAll() {
        return lineService.findAll();
    }

    @GetMapping("/lines/{id}")
    public LineResponse findById(@PathVariable Long id) {
        return lineService.findById(id);
    }

    @PutMapping("/lines/{id}") // 부분 업데이트라면 patch가 맞지 않을까?
    public void update(@RequestBody LineUpdateRequest request) {
        lineService.update(request);
    }
}
