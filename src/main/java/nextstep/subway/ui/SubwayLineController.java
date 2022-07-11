package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SubwayLineService;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubwayLineController {

	private final SubwayLineService lineService;

	@PostMapping("/lines")
	public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest request) {
		SubwayLineResponse lineResponse = lineService.createSubwayLine(request);
		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}

	@GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SubwayLineResponse>> findSubwayLines() {
		return ResponseEntity.ok().body(lineService.findAll());
	}

}