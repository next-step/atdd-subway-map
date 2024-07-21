package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.SectionService;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionRequest request) {
        var response = sectionService.save(lineId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping
    ResponseEntity<Void> deleteSection(@PathVariable Long lineId, Long stationId) {
        sectionService.delete(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
