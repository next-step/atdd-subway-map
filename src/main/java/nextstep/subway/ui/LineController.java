package nextstep.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<StationLineResponse> createStationLine(@RequestBody StationLineRequest stationLineRequest) {
		StationLineResponse stationLineResponse = lineService.createStationLines(stationLineRequest);
		return ResponseEntity.created(URI.create("/lines/" + stationLineResponse.getId())).body(stationLineResponse);
	}

	@GetMapping(value = "/lines")
	public ResponseEntity<List<StationLineResponse>> showAllStationsLines() {
		return ResponseEntity.ok().body(lineService.findAllStationLines());
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<StationLineResponse> showStationLine(@PathVariable Long id) {
		StationLineResponse stationLineResponse = lineService.findStationLine(id);
		if (ObjectUtils.isEmpty(stationLineResponse)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(stationLineResponse);
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateStationLine(@PathVariable Long id,
		@RequestBody StationLineRequest stationLineRequest) {
		lineService.updateStationLine(id, stationLineRequest);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteStationLine(@PathVariable Long id) {
		lineService.deleteStationLine(id);
		return ResponseEntity.noContent().build();
	}
}
