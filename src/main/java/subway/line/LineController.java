package subway.line;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.section.SectionCreateRequest;
import subway.section.SectionResponse;
import subway.section.SectionService;

@RestController
public class LineController {
	private LineService lineService;
	private SectionService sectionService;

	public LineController(LineService lineService, SectionService sectionService) {
		this.lineService = lineService;
		this.sectionService = sectionService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLineById(id));
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {
		lineService.updateLineById(id, lineUpdateRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/lines/{id}/sections")
	public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @RequestBody SectionCreateRequest sectionRequest) {
		SectionResponse section = sectionService.addSection(id, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + id + "/" + section.getId())).body(section);
	}
}
