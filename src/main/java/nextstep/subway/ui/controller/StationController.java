package nextstep.subway.ui.controller;

import nextstep.subway.applicaion.StationQueryService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class StationController {
	private final StationService stationService;
	private final StationQueryService stationQueryService;

	public StationController(StationService stationService, StationQueryService stationQueryService) {
		this.stationService = stationService;
		this.stationQueryService = stationQueryService;
	}

	@PostMapping("/stations")
	public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
		final StationResponse station = stationService.saveStation(stationRequest);
		return ResponseEntity
				.created(getCreateStatusHeader(station))
				.body(station);
	}

	@GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StationResponse>> showStations() {
		return ResponseEntity
				.ok()
				.body(stationQueryService.findAllStations());
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		stationService.deleteStationById(id);
		return ResponseEntity.noContent().build();
	}

	private URI getCreateStatusHeader(StationResponse station) {
		return URI.create("/stations/" + station.getId());
	}
}
