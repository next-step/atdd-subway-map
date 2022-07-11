package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SubwayLineRequest;
import nextstep.subway.applicaion.SubwayLineResponse;
import nextstep.subway.applicaion.SubwayLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SubwayLineController {

	private final SubwayLineService lineService;

	@PostMapping("/lines")
	public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest request) {
		SubwayLineResponse lineResponse = lineService.createSubwayLine(request);
		return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
	}
}