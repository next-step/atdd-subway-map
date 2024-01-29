package subway.line;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/lines")
@RestController
public class LineController {
	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineResponse> line(@PathVariable Long id) {
		LineResponse response = lineService.line(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> lines() {
		List<LineResponse> responses = lineService.lines();
		return ResponseEntity.ok(responses);
	}

	@PostMapping
	public ResponseEntity<LineResponse> saveLine(@RequestBody LineCreateRequest request) {
		LineResponse response = lineService.save(request);
		return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
		lineService.update(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
