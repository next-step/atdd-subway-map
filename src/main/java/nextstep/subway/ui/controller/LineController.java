package nextstep.subway.ui.controller;

import nextstep.subway.application.dto.request.LineRequest;
import nextstep.subway.application.dto.request.SectionRequest;
import nextstep.subway.application.dto.response.LineResponse;
import nextstep.subway.application.dto.response.SectionResponse;
import nextstep.subway.application.service.LineQueryService;
import nextstep.subway.application.service.LineService;
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
				.created(URI.create("/lines/" + line.getId()))
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

	@PostMapping("/lines/{lineId}/sections")
	public ResponseEntity<SectionResponse> createSection(@PathVariable long lineId, @RequestBody SectionRequest sectionRequest) {
		SectionResponse sectionResponse = lineService.saveSection(lineId, sectionRequest);

		return ResponseEntity
				.created(URI.create("/lines/" + lineId + "/section/" + sectionResponse.getId()))
				.body(sectionResponse);
	}

	@DeleteMapping("/lines/{lineId}/sections")
	public ResponseEntity<SectionResponse> removeLine(@PathVariable long lineId, @RequestParam long stationId) {
		lineService.deleteSection(lineId, stationId);

		return ResponseEntity
				.noContent()
				.build();
	}

}
