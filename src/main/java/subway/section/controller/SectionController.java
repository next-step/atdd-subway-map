package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.SectionDto;
import subway.section.dto.SectionRequest;
import subway.section.service.SectionService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity enrollSection(@PathVariable long id, @RequestBody SectionRequest sectionRequest) {
        SectionDto sectionDto = sectionService.enroll(id, sectionRequest.toDto());
        return ResponseEntity.created(URI.create("/lines/" + String.valueOf(id) + "/sections/" + sectionDto.getId())).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity deleteSection(@PathVariable long lineId, @RequestParam(value = "stationId") long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
