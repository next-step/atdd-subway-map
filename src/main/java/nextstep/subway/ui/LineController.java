package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		return null;
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> showLines() {
		return null;
	}

	@GetMapping("/lines/{lineId}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
		return null;
	}

	@PutMapping("/lines/{lineId}")
	public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
		return null;
	}

	@DeleteMapping("/lines/{lineId}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
		return null;
	}
}
