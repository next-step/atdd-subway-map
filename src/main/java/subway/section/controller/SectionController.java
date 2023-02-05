package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.SectionSaveRequest;
import subway.section.service.SectionService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody SectionSaveRequest saveRequest) {
        sectionService.saveSection(lineId, saveRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).build();
    }

    // 지하철 노선에 구간을 제거하는 기능 구현
    //지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
    //지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
    //새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.removeStationById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}