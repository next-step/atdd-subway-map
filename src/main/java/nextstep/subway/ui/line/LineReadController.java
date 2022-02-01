package nextstep.subway.ui.line;

import java.util.List;
import nextstep.subway.applicaion.dto.LineReadResponse;
import nextstep.subway.applicaion.line.LineReadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lines")
public class LineReadController {
    private final LineReadService lineReadService;

    public LineReadController(LineReadService lineReadService) {
        this.lineReadService = lineReadService;
    }

    @GetMapping
    public ResponseEntity<List<LineReadResponse>> getAllLines() {
        List<LineReadResponse> lines = lineReadService.findAllLines();
        return ResponseEntity.ok().body(lines);
    }

    @GetMapping("{id}")
    public ResponseEntity<LineReadResponse> getLine(@PathVariable Long id) {
        LineReadResponse line = lineReadService.findSpecificLine(id);
        System.out.println("line = " + line);
        return ResponseEntity.ok().body(line);
    }
}
