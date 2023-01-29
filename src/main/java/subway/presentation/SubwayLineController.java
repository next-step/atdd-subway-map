package subway.presentation;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.application.SubwayLineService;
import subway.presentation.request.SubwayLineRequest;
import subway.presentation.response.SubwayLineResponse;

@RequestMapping("/lines")
@RestController
@RequiredArgsConstructor
public class SubwayLineController {

	private final SubwayLineService subwayLineService;

	@PostMapping
	public ResponseEntity<SubwayLineResponse.CreateInfo> createSubwayLine(
		@RequestBody SubwayLineRequest.Create createRequest) {
		SubwayLineResponse.CreateInfo createInfo = subwayLineService.createSubwayLine(createRequest);

		return ResponseEntity.created(URI.create("/lines/" + createInfo.getId()))
			.body(createInfo);
	}

	@GetMapping
	public ResponseEntity<List<SubwayLineResponse.LineInfo>> findSubwayLines() {
		return ResponseEntity.ok(subwayLineService.findSubwayLines());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubwayLineResponse.LineInfo> findSubwayLine(@PathVariable Long id) {
		return ResponseEntity.ok(subwayLineService.findSubwayLineById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateSubwayLine(
		@PathVariable Long id,
		@RequestBody SubwayLineRequest.Update updateRequest) {
		subwayLineService.update(id, updateRequest);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
		subwayLineService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
