package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SectionController {

	private final SectionService sectionService;

	@PostMapping("/lines/{subwayLineId}/sections")
	public ResponseEntity<Void> registerSection(@PathVariable Long subwayLineId, @RequestBody SectionRequest request) {
		sectionService.saveSection(subwayLineId, request);
		return ResponseEntity.ok().build();
	}
}
