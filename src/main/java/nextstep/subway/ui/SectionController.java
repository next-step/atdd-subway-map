package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SectionController {

	private final SectionService sectionService;

	@PostMapping("/lines/{subwayLineId}/sections")
	public ResponseEntity<Void> registerSection(@PathVariable Long subwayLineId, @RequestBody SectionRequest request) {
		sectionService.saveSection(subwayLineId, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/lines/{subwayLineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long subwayLineId, @RequestParam("stationId") Long stationId) {
		sectionService.delete(subwayLineId, stationId);
		return ResponseEntity.noContent().build();
	}
}
