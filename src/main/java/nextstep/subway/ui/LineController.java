package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LineController {
	private final LineService lineService;

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
		LineResponse line = lineService.saveLine(lineRequest);
		return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
	}

	@GetMapping(value = "/lines", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok().body(lineService.findAllLines());
	}

	@GetMapping(value = "/lines/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLineById(id));
	}

	@PutMapping(value = "/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest updateRequest) {
		lineService.updateLine(id, updateRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}
}
