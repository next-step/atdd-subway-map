package nextstep.subway.ui.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

@RestController
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping("/stations")
	public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
		Station station = stationService.saveStation(stationRequest.toEntity());
		return ResponseEntity
			.created(getCreateStatusHeader(station))
			.body(toResponse(station));
	}

	@GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StationResponse>> showStations() {
		return ResponseEntity
			.ok()
			.body(toListResponse(stationService.findAllStations()));
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		stationService.deleteStationById(id);
		return ResponseEntity.noContent().build();
	}

	private URI getCreateStatusHeader(Station station) {
		return URI.create("/stations/" + station.getId());
	}

	private StationResponse toResponse(Station station) {
		return StationResponse.from(station);
	}

	private List<StationResponse> toListResponse(List<Station> allStations) {
		return allStations.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
