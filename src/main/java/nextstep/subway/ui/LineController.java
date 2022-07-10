package nextstep.subway.ui;

import java.net.URI;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(path = "/lines")
    public ResponseEntity<?> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse savedLine = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + savedLine.getId())).body(savedLine);
    }

}
