package nextstep.subway.ui;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		List<StationResponse> stations = new ArrayList<>();
		StationResponse station1 = new StationResponse(1L, "지하철역");
		StationResponse station2 = new StationResponse(2L, "새로운지하철역");
		stations.add(station1);
		stations.add(station2);
		LineResponse line = new LineResponse(1L, "신분당선", "bg-red-600", stations);
//		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LineResponse>> showLines() {
		List<StationResponse> stations1 = new ArrayList<>();
		List<StationResponse> stations2 = new ArrayList<>();
		StationResponse station1 = new StationResponse(1L, "지하철역");
		StationResponse station2 = new StationResponse(2L, "새로운지하철역");
		StationResponse station3 = new StationResponse(3L, "또다른지하철역");
		stations1.add(station1);
		stations1.add(station2);
		stations2.add(station1);
		stations2.add(station3);

		List<LineResponse> lines = new ArrayList<>();
		LineResponse line1 = new LineResponse(1L, "신분당선", "bg-red-600", stations1);
		LineResponse line2 = new LineResponse(2L, "분당선", "bg-green-600", stations2);
		lines.add(line1);
		lines.add(line2);

		return ResponseEntity.ok().body(lines);
	}

	@GetMapping("/lines/{lineId}")
	public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
		List<StationResponse> stations1 = new ArrayList<>();
		StationResponse station1 = new StationResponse(1L, "지하철역");
		StationResponse station2 = new StationResponse(2L, "새로운지하철역");
		stations1.add(station1);
		stations1.add(station2);

		LineResponse line = new LineResponse(lineId, "신분당선", "bg-red-600", stations1);

		return ResponseEntity.ok().body(line);
	}

	@PutMapping("/lines/{lineId}")
	public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/lines/{lineId}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
		return ResponseEntity.noContent().build();
	}
}
