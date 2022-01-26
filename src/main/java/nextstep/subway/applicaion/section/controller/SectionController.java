package nextstep.subway.applicaion.section.controller;

import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.section.dto.SectionResponse;
import nextstep.subway.applicaion.section.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SectionController {

	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@PostMapping("/lines/{lineId}/sections")
	public ResponseEntity<SectionResponse> createLine(@PathVariable long lineId, @RequestBody SectionRequest sectionRequest) {
		SectionResponse sectionResponse = sectionService.saveSection(lineId, sectionRequest);

		return ResponseEntity
				.created(getCreateStatusHeader(lineId, sectionResponse))
				.body(sectionResponse);
	}

	@DeleteMapping("/lines/{lineId}/sections")
	public ResponseEntity<SectionResponse> createLine(@PathVariable long lineId, @RequestParam long stationId) {
		sectionService.removeSection(lineId, stationId);

		return ResponseEntity
				.noContent()
				.build();
	}

	private URI getCreateStatusHeader(long lineId, SectionResponse sectionResponse) {
		return URI.create("/lines/" + lineId + "/section/" + sectionResponse.getId());
	}
}
