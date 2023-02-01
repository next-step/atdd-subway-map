package subway.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.application.SectionService;
import subway.presentation.request.SectionRequest;

@RequestMapping("/lines/{lineId}/sections")
@RestController
@RequiredArgsConstructor
public class SectionController {

	private final SectionService sectionService;

	@PostMapping
	public ResponseEntity<Void>  createSection(
		@PathVariable Long lineId,
		@RequestBody SectionRequest.Create createRequest) {

		Long sectionId = sectionService.createSection(lineId, createRequest);

		return ResponseEntity.created(URI.create("/sections/" + sectionId)).build();
	}
}
