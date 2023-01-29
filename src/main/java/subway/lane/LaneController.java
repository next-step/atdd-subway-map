package subway.lane;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LaneController {

  private LaneService laneService;

  public LaneController(LaneService laneService) {
    this.laneService = laneService;
  }

  // 노선 생성
  @PostMapping("/lane")
  public ResponseEntity<LaneResponse> createLane(@RequestBody LaneRequest request) {
    return ResponseEntity.ok().body(laneService.saveLane(request));
  }

  // 노선 조회
  @GetMapping("/lane/{id}")
  public ResponseEntity<LaneResponse> showLane(@PathVariable Long id) {
    LaneResponse response = laneService.showLane(id);
    if (response == null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(response);
  }

  // 노선 목록 조회
  @GetMapping("/lanes")
  public ResponseEntity<List<LaneResponse>> showAllLanes() {
    return ResponseEntity.ok().body(laneService.showLanes());
  }

  // 노선 수정
  @PatchMapping("/lane/{id}")
  public ResponseEntity<LaneResponse> patchLane(@PathVariable Long id, @RequestBody LaneRequest request) {
    LaneResponse response = laneService.updateLane(id,request);
    if (response == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(response);
  }

  // 노선 삭제
  @DeleteMapping("/lane/{id}")
  public ResponseEntity<LaneResponse> deleteLane(@PathVariable Long id) {
    laneService.deleteLaneById(id);
    return ResponseEntity.noContent().build();
  }
}
