package nextstep.subway.applicaion.line.controller;

import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.line.service.LineQueryService;
import nextstep.subway.applicaion.line.service.LineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

	private final LineService lineService;
	private final LineQueryService lineQueryServiceService;

	public LineController(LineService lineService, LineQueryService lineQueryServiceService) {
		this.lineService = lineService;
		this.lineQueryServiceService = lineQueryServiceService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		final LineResponse line = lineService.saveLine(lineRequest);

		return ResponseEntity
				.created(getCreateStatusHeader(line))
				.body(line);
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> getAllLines() {
		final List<LineResponse> allLines = lineQueryServiceService.getAllLines();
		return ResponseEntity
				.ok()
				.body(allLines);
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineResponse> getLine(@PathVariable long id) {
		final LineResponse line = lineQueryServiceService.getLine(id);
		return ResponseEntity
				.ok()
				.body(line);
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<LineResponse> updateLine(@PathVariable long id, @RequestBody LineRequest lineRequest) {
		final LineResponse line = lineService.updateLine(id, lineRequest);
		return ResponseEntity
				.ok()
				.body(line);
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable long id) {
		lineService.deleteLine(id);
		return ResponseEntity
				.noContent()
				.build();
	}

	private URI getCreateStatusHeader(LineResponse line) {
		return URI.create("/lines/" + line.getId());
	}

}
