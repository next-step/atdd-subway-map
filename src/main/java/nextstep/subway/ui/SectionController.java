package nextstep.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;

@RestController
@RequestMapping("/lines")
public class SectionController {

	private SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping(value = "{lineId}/section")
	public ResponseEntity<SectionResponse> createSection(@PathVariable long lineId,
		@RequestBody SectionRequest sectionRequest) {
		SectionResponse sectionResponse = sectionService.createSection(lineId, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + lineId + "/section")).body(sectionResponse);
	}

	@DeleteMapping(value = "{lineId}/section")
	public ResponseEntity<Void> deleteSection(@PathVariable long lineId, @RequestParam("stationId") long stationId) {
		sectionService.deleteSection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "{lineId}/section")
	public ResponseEntity<List<SectionResponse>> getSectionList(@PathVariable long lineId) {
		return ResponseEntity.ok().body(sectionService.getSectionList(lineId));
	}

	@GetMapping(value = "/section/{sectionId}")
	public ResponseEntity<SectionResponse> getSection(@PathVariable long sectionId) {
		return ResponseEntity.ok().body(sectionService.getSection(sectionId));
	}

}
