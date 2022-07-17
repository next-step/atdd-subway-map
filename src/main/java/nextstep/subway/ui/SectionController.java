package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectionController {

	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long StationId) {
		return ResponseEntity.noContent().build();
	}
}
