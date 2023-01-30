package subway.line;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

  private LineService lineService;

  public LineController(LineService lineService) {
    this.lineService = lineService;
  }

  // 노선 생성
  @PostMapping("/line")
  public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
    return ResponseEntity.ok().body(lineService.saveLine(request));
  }

  // 노선 조회
  @GetMapping("/line/{id}")
  public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
    LineResponse response = lineService.showLine(id);
    if (response == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(response);
  }

  // 노선 목록 조회
  @GetMapping("/lines")
  public ResponseEntity<List<LineResponse>> showAllLines() {
    return ResponseEntity.ok().body(lineService.showLines());
  }

  // 노선 수정
  @PatchMapping("/line/{id}")
  public ResponseEntity<LineResponse> patchLine(@PathVariable Long id, @RequestBody LineRequest request) {
    LineResponse response = lineService.updateLine(id,request);
    if (response == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(response);
  }

  // 노선 삭제
  @DeleteMapping("/line/{id}")
  public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
    lineService.deleteLineById(id);
    return ResponseEntity.noContent().build();
  }
}
