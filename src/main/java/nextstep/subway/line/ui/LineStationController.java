package nextstep.subway.line.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.station.dto.LineStationCreateRequest;

@RestController
@RequestMapping("/lines")
public class LineStationController {
	private final LineStationService lineStationService;

	public LineStationController(LineStationService lineStationService) {
		this.lineStationService = lineStationService;
	}

	@PostMapping("/{lineId}/stations")
	public ResponseEntity<Void> createLineStation(@PathVariable Long lineId,
		@RequestBody LineStationCreateRequest dto) {
		dto.setLineId(lineId);
		lineStationService.registerLineStation(dto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> unregisterLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
		lineStationService.unregisterLineStation(lineId, stationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
