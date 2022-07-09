package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.ui.dto.LineRequest;
import nextstep.subway.ui.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineDto lineDto = lineService.createLine(LineCreateDto.of(lineRequest));
        LineResponse response = LineResponse.of(lineDto);

        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }
}
