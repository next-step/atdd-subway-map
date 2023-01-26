package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
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
        return lineService.findAllLineList();
    }
}
