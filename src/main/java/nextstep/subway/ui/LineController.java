package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineApiService;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.ui.dto.LineRequest;
import nextstep.subway.ui.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineApiService lineApiService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid  LineRequest lineRequest) {
        long lineId = lineApiService.createLine(lineRequest.toCreateDto());
        LineDto lineDto = lineApiService.getLine(lineId);

        LineResponse response = LineResponse.of(lineDto);

        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineDto> lineDtos = lineApiService.getLines();

        return ResponseEntity.ok(lineDtos.stream()
                .map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList()));
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineDto lineDto = lineApiService.getLine(id);
        LineResponse response = LineResponse.of(lineDto);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable Long id,
            @RequestBody LineRequest lineRequest
    ) {
        lineApiService.updateLine(id, lineRequest.toUpdateDto());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineApiService.deleteLine(id);

        return ResponseEntity.noContent().build();
    }
}
