package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionDeleteRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.exception.SubwayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SectionController {

	private final LineService lineService;

	public SectionController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
		try {
			lineService.addSection(lineId, sectionRequest);
		} catch(SubwayException e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("lines/{lineId}/sections")
	public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
		try {
			lineService.deleteSection(lineId, sectionDeleteRequest);
		} catch (SubwayException e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.noContent().build();
	}
}
