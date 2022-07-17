package nextstep.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.application.dto.AppendSectionRequest;

@RequestMapping("lines/{lineId}/sections")
@RestController
public class SectionController {

	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping
	public ResponseEntity<Void> appendSection(@PathVariable Long lineId, @RequestBody AppendSectionRequest request) {
		sectionService.appendSection(lineId, request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
		sectionService.deleteSection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}
}
