package subway.section;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectionController {
	private SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping("/lines/{id}/sections")
	public ResponseEntity<SectionResponse> createSection(@PathVariable Long id,
		@RequestBody SectionCreateRequest sectionRequest) {
		SectionResponse section = sectionService.addSection(id, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + id + "/" + section.getId())).body(section);
	}

	@DeleteMapping("/lines/{id}/sections")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id, @RequestParam Long stationId) {
		sectionService.deleteSectionById(id, stationId);
		return ResponseEntity.noContent().build();
	}
}
