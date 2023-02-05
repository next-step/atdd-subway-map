package subway.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.api.dto.LineRequest;
import subway.api.dto.LineResponse;
import subway.api.dto.SectionRequest;
import subway.domain.service.LineService;
import subway.domain.service.SectionService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
	private LineService lineService;
	private SectionService sectionService;

	public LineController(LineService lineService, SectionService sectionService) {
		this.lineService = lineService;
		this.sectionService = sectionService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping(value = "/lines")
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping(value = "/lines/{id}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLine(id));
	}

	@PutMapping(value = "/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
		lineService.updateLine(id,lineRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/lines/{id}/sections")
	public ResponseEntity<Void> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
		sectionService.addSection(id, sectionRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/lines/{id}/sections")
	public ResponseEntity<Long> deleteSection(@PathVariable Long id, @RequestBody Long stationId) {
		sectionService.deleteSection(id, stationId);
		return ResponseEntity.noContent().build();
	}
}
