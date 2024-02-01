package subway.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.service.LineService;

@RestController
public class LineController {
	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createStation(@RequestBody LineRequest request) {
		LineResponse response = lineService.saveLines(request);
		return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
	}
}
