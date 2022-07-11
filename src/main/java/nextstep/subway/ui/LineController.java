package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.ui.dto.LineRequest;
import nextstep.subway.ui.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Long lineId = lineService.createLine(lineRequest.toCreateDto());

        LineDto lineDto = lineService.getLine(lineId);
        LineResponse response = LineResponse.of(lineDto);

        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> getLines() {
        List<LineDto> lineDtos = lineService.getLines();

        return ResponseEntity.ok(lineDtos.stream()
                .map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList()));
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
        LineDto lineDto = lineService.getLine(id);
        LineResponse response = LineResponse.of(lineDto);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> updateLine(
            @PathVariable Long id,
            @RequestBody LineRequest lineRequest
    ) {
        lineService.updateLine(id, lineRequest.toUpdateDto());

        return ResponseEntity.ok().build();
    }
}
