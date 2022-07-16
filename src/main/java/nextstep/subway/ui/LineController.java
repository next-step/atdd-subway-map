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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.CreateLineResponse;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.ModifyLineRequest;

@RequestMapping("/lines")
@RestController
public class LineController {

	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
		CreateLineResponse response = lineService.createLine(request);
		return ResponseEntity.created(URI.create("/lines" + response.getId()))
			.body(response);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> getLines() {
		List<LineResponse> response = lineService.getLines();
		return ResponseEntity.ok(response);

	}

	@GetMapping("/{id}")
	public ResponseEntity<LineResponse> getLine(@PathVariable Long id) {
		LineResponse response = lineService.getLine(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyLine(@PathVariable Long id, @RequestBody ModifyLineRequest request) {
		lineService.modifyLine(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}

}
