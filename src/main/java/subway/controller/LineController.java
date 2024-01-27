package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse lineResponse = lineService.saveLine(lineRequest);

		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}

	@GetMapping("/lines")
	ResponseEntity<List<LineResponse>> getLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping("/lines/{id}")
	ResponseEntity<LineResponse> getLineById(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLineById(id));
	}
}
