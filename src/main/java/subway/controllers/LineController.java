package subway.controllers;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.models.Line;
import subway.services.LineService;

@RestController
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest.Create lineRequest) {
        Line line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
            .body(LineResponse.of(line));
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showStations() {
        return ResponseEntity.ok().body(LineResponse.of(lineService.findAllLines()));
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok().body(LineResponse.of(lineService.findById(id)));
    }

    @PutMapping(value = "/lines/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable Long id,
        @RequestBody LineRequest.Update lineRequest) {
        lineService.update(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
