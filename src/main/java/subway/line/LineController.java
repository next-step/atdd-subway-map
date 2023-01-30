package subway.line;

import java.util.List;
import java.util.Optional;
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

  private final LineService lineService;

  public LineController(LineService lineService) {
    this.lineService = lineService;
  }

  /**
   * 새로운 지하철 노선을 추가한다.
   * @param request LineRequest
   * @return 노선 생성의 결과를 반환한다.
   */
  @PostMapping("/lines")
  public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
    return ResponseEntity.ok().body(lineService.saveLine(
        new Line(request.getName(), request.getInbound(), request.getOutbound())
    ));
  }

  /**
   * 지하철의 노선을 조회한다
   * @param id Long. 조회하려는 노선의 ID
   * @return 지하철 노선의 조회 결과 값을 반환한다.
   */
  @GetMapping("/lines/{id}")
  public ResponseEntity<LineResponse> showLine(@PathVariable Long id) {
    Optional<LineResponse> response = lineService.showLine(id);
    if (response.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(response.get());
  }

  /**
   * 모든 지하철의 노선을 조회한다.
   * @return 모든 지하철 모든의 정보를 반환한다.
   */
  @GetMapping("/lines")
  public ResponseEntity<List<LineResponse>> showAllLines() {
    return ResponseEntity.ok().body(lineService.showLines());
  }

  /**
   * 특정 지하철 노선의 정보를 변경한다.
   * @param id Long. 변경하려는 지하철 노선의 ID
   * @param request
   * @return 변환된 지하철 정보를 반환한다. 존재하지 않는 지하철 노선에 대한 변경은 noContent()를 반환한다.
   */
  @PatchMapping("/lines/{id}")
  public ResponseEntity<LineResponse> patchLine(@PathVariable Long id, @RequestBody LineRequest request) {
    Optional<LineResponse> response = lineService.updateLine(id,request);
    if (response.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(response.get());
  }

  /**
   * id에 해당하는 노선 정보를 삭제한다.
   * @param id Long. 삭제하려는 노선의 ID
   * @return noContent() 결과값
   */
  @DeleteMapping("/lines/{id}")
  public ResponseEntity<LineResponse> deleteLine(@PathVariable Long id) {
    lineService.deleteLineById(id);
    return ResponseEntity.noContent().build();
  }
}
