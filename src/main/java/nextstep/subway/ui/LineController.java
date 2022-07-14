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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;

@RestController
@RequestMapping("/lines")
public class LineController {
	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse lineResponse = lineService.createLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> getAllLine() {
		return ResponseEntity.ok(lineService.getAllLine());
	}

	@GetMapping("/{lineId}")
	public ResponseEntity<LineResponse> getLine(@PathVariable("lineId") Long lineId) {
		return ResponseEntity.ok(lineService.getLine(lineId));
	}

	@DeleteMapping("/{lineId}")
	public ResponseEntity<Void> deleteLine(@PathVariable("lineId") Long lineId) {
		lineService.deleteLine(lineId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{lineId}")
	public ResponseEntity<Void> updateLine(@PathVariable("lineId") Long lineId, @RequestBody LineUpdateRequest lineUpdateRequest) {
		lineService.updateLine(lineId, lineUpdateRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{lineId}/sections")
	public ResponseEntity<Void> addSection(@PathVariable("lineId") Long lineId, @RequestBody SectionRequest sectionRequest) {
		LineResponse lineResponse = lineService.addSection(lineId, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/1/sections/" + lineResponse.getId())).build();
	}

	@DeleteMapping("/{lineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable("lineId") Long lineId, @RequestParam("stationId") Long stationId) {
		lineService.deleteSection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}
}
