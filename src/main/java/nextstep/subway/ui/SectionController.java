package nextstep.subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;

@RestController
@RequestMapping("/lines")
public class SectionController {
	private LineService lineService;

	public SectionController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/{id}/sections")
	public ResponseEntity<SectionResponse> addSection(@PathVariable(name = "id") Long lineId, @RequestBody SectionRequest sectionRequest) {
		lineService.addSection(lineId, sectionRequest);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
		lineService.deleteSection(id, stationId);
		return ResponseEntity.noContent().build();
	}
}
