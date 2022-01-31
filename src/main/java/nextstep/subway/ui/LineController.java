package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;

import org.springframework.http.HttpStatus;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> getLines() {
		List<LineResponse> lineResponses = lineService.findAllLines();
		return ResponseEntity.ok().body(lineResponses);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<LineResponse> getLine(@PathVariable("id") Long id) {
		LineResponse lineResponse = lineService.findById(id);
		return ResponseEntity.ok().body(lineResponse);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable("id") Long id, @RequestBody LineRequest lineRequest) {
		lineService.updateLine(id, lineRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable("id") Long id) {
		lineService.deleteLine(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{lineId}/sections")
	public ResponseEntity<LineResponse> addSection(@PathVariable("lineId") Long lineId, @RequestBody SectionRequest sectionRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(lineService.addNewSection(lineId, sectionRequest));
	}

	@DeleteMapping("/{lineId}/sections")
	public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId){
		lineService.removeSectionByStationId(lineId, stationId);
		return ResponseEntity.ok().build();
	}
}
