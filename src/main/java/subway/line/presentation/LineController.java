package subway.line.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import subway.line.application.LineService;
import subway.line.application.dto.request.LineRequest;
import subway.line.application.dto.response.LineCreateResponse;
import subway.line.application.dto.response.LineResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
class LineController {

    private final LineService lineService;

    LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LineCreateResponse createLine(@RequestBody final LineRequest lineRequest) {
        return lineService.saveLine(lineRequest);
    }

    @GetMapping
    List<LineResponse> showLines() {
        return lineService.findAllLines();
    }
}
