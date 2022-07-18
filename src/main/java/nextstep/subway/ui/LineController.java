package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.LineService;
import nextstep.subway.ui.dto.line.CreateLineRequest;
import nextstep.subway.ui.dto.line.LineResponse;
import nextstep.subway.ui.dto.line.UpdateLineRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStationLine(@RequestBody CreateLineRequest request) {
        LineResponse response = lineService.saveLine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/lines")
    public List<LineResponse> findLineAll() {
        return lineService.findAllLines();
    }

    @GetMapping("/lines/{lineId}")
    public LineResponse findLineAll(@PathVariable Long lineId) {
        return lineService.findLineById(lineId);
    }

    @PutMapping("/lines/{lineId}")
    public void updateLine(@PathVariable Long lineId, @RequestBody UpdateLineRequest request) {
        lineService.updateLine(lineId, request);
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId) {
        lineService.deleteLine(lineId);
        return ResponseEntity.noContent().build();
    }
}
