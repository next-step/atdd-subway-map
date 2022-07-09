package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.ui.dto.LineRequest;
import nextstep.subway.ui.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineDto lineDto = lineService.createLine(lineRequest.toDto());
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
}
