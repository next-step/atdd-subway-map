package nextstep.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;

@RestController
public class StationLineController {
	private StationLineService stationLineService;

	public StationLineController(StationLineService stationLineService) {
		this.stationLineService = stationLineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationLineRequest) {
		StationLineResponse stationLineResponse = stationLineService.createStationLines(stationLineRequest);
		return ResponseEntity.created(URI.create("/lines/" + stationLineResponse.getId())).body(stationLineResponse);
	}

	@GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StationLineResponse>> showAllStationsLines() {
		return ResponseEntity.ok().body(stationLineService.findAllStationLines());
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<StationLineResponse> showStationLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(stationLineService.findStationLine(id));
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateStationLine(@PathVariable Long id,
		@RequestBody StationLineRequest stationLineRequest) {
		stationLineService.updateStationLine(id, stationLineRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
		stationLineService.deleteStationLine(id);
		return ResponseEntity.noContent().build();
	}
}
