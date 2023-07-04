package subway.controller;

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
import subway.domain.StationLineSectionCreateRequest;
import subway.service.StationLineService;
import subway.service.dto.StationLineCreateRequest;
import subway.service.dto.StationLineResponse;
import subway.service.dto.StationLineUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class StationLineController {
	private final StationLineService stationLineService;

	@PostMapping
	public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineCreateRequest request) {
		return ResponseEntity.ok(stationLineService.createStationLine(request));
	}

	@GetMapping
	public ResponseEntity<List<StationLineResponse>> getStationLines() {
		return ResponseEntity.ok(stationLineService.getStationLines());
	}

	@GetMapping("/{lineId}")
	public ResponseEntity<StationLineResponse> getStationLine(@PathVariable Long lineId) {
		final StationLineResponse result = stationLineService.getStationLine(lineId);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{lineId}")
	public ResponseEntity<?> updateStationLine(@PathVariable Long lineId, @RequestBody StationLineUpdateRequest request) {
		stationLineService.updateStationLine(lineId, request);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{lineId}")
	public ResponseEntity<?> deleteStationLine(@PathVariable Long lineId) {
		stationLineService.deleteStationLine(lineId);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/{lineId}/sections")
	public ResponseEntity<?> createStationLineSection(@PathVariable Long lineId, @RequestBody StationLineSectionCreateRequest request) {
		stationLineService.createStationLineSection(lineId, request);

		return ResponseEntity.ok().build();
	}
}
