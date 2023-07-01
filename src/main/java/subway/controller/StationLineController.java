package subway.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.service.StationLineService;
import subway.service.dto.StationLineRequest;
import subway.service.dto.StationLineResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class StationLineController {
	private final StationLineService stationLineService;

	@PostMapping
	public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest request) {
		return ResponseEntity.ok(stationLineService.createStationLine(request));
	}

	@GetMapping
	public ResponseEntity<List<StationLineResponse>> getStationLines() {
		return ResponseEntity.ok(stationLineService.getStationLines());
	}
}
