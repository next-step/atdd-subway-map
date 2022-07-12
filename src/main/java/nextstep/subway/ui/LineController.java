package nextstep.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest LineRequest) {
		LineResponse LineResponse = lineService.createLines(LineRequest);
		return ResponseEntity.created(URI.create("/lines/" + LineResponse.getId())).body(LineResponse);
	}

	@GetMapping(value = "/lines")
	public ResponseEntity<List<LineResponse>> showAllStationsLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLine(id));
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id,
		@RequestBody LineRequest LineRequest) {
		lineService.updateLine(id, LineRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}
}
