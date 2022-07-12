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

	private SectionService selectionService;

	public SectionController(SectionService selectionService) {
		this.selectionService = selectionService;
	}

	@PostMapping(value = "{lineId}/selection")
	public ResponseEntity<SectionResponse> createSection(@PathVariable long lineId,
		@RequestBody SectionRequest sectionRequest) {
		SectionResponse sectionResponse = selectionService.createSelection(lineId, sectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + lineId + "/selection")).body(sectionResponse);
	}

	@DeleteMapping(value = "{lineId}/selection")
	public ResponseEntity<Void> deleteSelection(@PathVariable long lineId, @RequestParam("stationId") long stationId) {
		selectionService.deleteSelection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "{lineId}/selection")
	public ResponseEntity<List<SectionResponse>> getSelectionList(@PathVariable long lineId) {
		return ResponseEntity.ok().body(selectionService.getSelectionList(lineId));
	}

	@GetMapping(value = "/selection/{selectionId}")
	public ResponseEntity<SectionResponse> getSelection(@PathVariable long selectionId) {
		return ResponseEntity.ok().body(selectionService.getSelection(selectionId));
	}

}
