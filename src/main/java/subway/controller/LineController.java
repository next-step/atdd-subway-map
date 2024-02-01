package subway.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.UpdateLineRequest;
import subway.service.LineService;

@RestController
public class LineController {
	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
		LineResponse response = lineService.saveLines(request);
		return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> getLines() {
		List<LineResponse> response = lineService.getLines();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
		LineResponse response = lineService.getLine(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateLine(
			@PathVariable Long id, @RequestBody UpdateLineRequest request) {
		lineService.updateLine(id, request);
		return ResponseEntity.ok().build();
	}
}
