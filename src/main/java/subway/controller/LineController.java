package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
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

	@PutMapping("/lines/{id}")
	ResponseEntity<String> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
		if(lineRequest.getName().isEmpty()) {
			return ResponseEntity.badRequest().body("이름 값이 빈 값일 수 없습니다.");
		}

		if(lineRequest.getColor().isEmpty()) {
			return ResponseEntity.badRequest().body("색상 값이 빈 값일 수 없습니다.");
		}

		lineService.updateLine(id, lineRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/lines/{id}")
	ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/lines/{id}/sections")
	ResponseEntity<String> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
		lineService.createSection(id, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + id)).build();
	}

	@DeleteMapping("/lines/{id}/sections")
	ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
		lineService.deleteSection(id, stationId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/lines/{id}/sections")
	ResponseEntity<List<SectionResponse>> getSections(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findSectionsByLine(id));
	}
}
