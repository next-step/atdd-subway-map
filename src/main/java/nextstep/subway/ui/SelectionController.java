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

import nextstep.subway.applicaion.SelectionService;
import nextstep.subway.applicaion.dto.SelectionRequest;
import nextstep.subway.applicaion.dto.SelectionResponse;

@RestController
@RequestMapping("/lines")
public class SelectionController {

	private SelectionService selectionService;

	public SelectionController(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	@PostMapping(value = "{lineId}/selection")
	public ResponseEntity<SelectionResponse> createSelection(@PathVariable long lineId,
		@RequestBody SelectionRequest selectionRequest) {
		SelectionResponse selectionResponse = selectionService.createSelection(lineId, selectionRequest);
		return ResponseEntity.created(URI.create("/lines/" + lineId + "/selection")).body(selectionResponse);
	}

	@DeleteMapping(value = "{lineId}/selection")
	public ResponseEntity<Void> deleteSelection(@PathVariable long lineId, @RequestParam("stationId") long stationId) {
		selectionService.deleteSelection(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "{lineId}/selection")
	public ResponseEntity<List<SelectionResponse>> getSelectionList(@PathVariable long lineId) {
		return ResponseEntity.ok().body(selectionService.getSelectionList(lineId));
	}

	@GetMapping(value = "/selection/{selectionId}")
	public ResponseEntity<SelectionResponse> getSelection(@PathVariable long selectionId) {
		return ResponseEntity.ok().body(selectionService.getSelection(selectionId));
	}

}
