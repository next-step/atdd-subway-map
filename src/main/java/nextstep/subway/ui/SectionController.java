package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectionController {

	private final LineService lineService;

	public SectionController(LineService lineService) {
		this.lineService = lineService;
	}


	@PostMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
		lineService.addSection(lineId, sectionRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long StationId) {
		return ResponseEntity.noContent().build();
	}
}
