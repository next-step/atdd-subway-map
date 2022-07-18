package nextstep.subway.ui;

import java.net.URI;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;

@RestController
@RequiredArgsConstructor
public class SectionController {
	private final SectionService sectionService;

	@PostMapping("/lines/{id}/sections")
	public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
		sectionService.addSection(id, sectionRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(value = "/lines/{id}/sections")
	public ResponseEntity<Void> subSection(@PathVariable Long id, @RequestParam Long stationId) {
		sectionService.subSection(id, stationId);
		return ResponseEntity.noContent().build();
	}
}
