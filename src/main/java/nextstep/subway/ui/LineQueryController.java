package nextstep.subway.ui;

import nextstep.subway.applicaion.LineQueryService;
import nextstep.subway.applicaion.dto.LineReadAllResponse;
import nextstep.subway.applicaion.dto.LineReadResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineQueryController {
    private LineQueryService lineQueryService;

    public LineQueryController(LineQueryService lineService) {
        this.lineQueryService = lineService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineReadAllResponse>> findAllLine() {
        return ResponseEntity.ok(lineQueryService.findAllLine());
    }

    @GetMapping(value = {"{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LineReadResponse> findLine(@PathVariable final Long id) {
        return ResponseEntity.ok(lineQueryService.findLine(id));
    }
}
