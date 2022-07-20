package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.exception.SubwayException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping("/{lineId}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
		return ResponseEntity.ok().body(lineService.findLineById(lineId));
	}

	@PutMapping("/{lineId}")
	public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineUpdateRequest lineUpdateRequest) {
		lineService.updateLine(lineId, lineUpdateRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{lineId}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
		lineService.deleteLineById(lineId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{lineId}/sections")
	public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
		lineService.addSection(lineId, sectionRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{lineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
		lineService.deleteSection(lineId, sectionDeleteRequest);
		return ResponseEntity.noContent().build();
	}
}
