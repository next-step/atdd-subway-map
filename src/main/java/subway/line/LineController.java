package subway.line;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.entity.Line;
import subway.line.service.LineService;

@RestController
@RequiredArgsConstructor
public class LineController {

	private final LineService lineService;

	@PostMapping("/lines")
	public ResponseEntity<LineResponse> save(@RequestBody LineCreateRequest lineCreateRequest) {
		Line saved = lineService.save(lineCreateRequest);
		return ResponseEntity.created(URI.create("/lines/" + saved.getId())).body(LineResponse.from(saved));
	}

	@GetMapping("/lines")
	public List<LineResponse> findAll() {
		return lineService.findAll();
	}

	@GetMapping("/lines/{id}")
	public LineResponse findById(@PathVariable Long id) {
		return lineService.findById(id);
	}

	@PatchMapping("/lines/{id}") // 부분 업데이트라면 patch가 맞지 않을까?
	public void update(@RequestBody LineUpdateRequest request) {
		lineService.update(request);
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id) {
		lineService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
