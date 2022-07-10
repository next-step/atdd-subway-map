package nextstep.subway.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;

@RestController
public class StationLineController {
	private StationService stationService;

	public StationLineController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping("/lines")
	public ResponseEntity<Void> createStationLine(@RequestBody StationRequest stationRequest) {
		return null;
	}

	@GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> showStationsLine() {
		return null;
	}

	@GetMapping("/stations/{id}")
	public ResponseEntity<Void> showStationLine(@PathVariable Long id) {
		return null;
	}

	@PutMapping("/stations/{id}")
	public ResponseEntity<Void> updateStationLine(@PathVariable Long id) {
		return null;
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
		return null;
	}
}
