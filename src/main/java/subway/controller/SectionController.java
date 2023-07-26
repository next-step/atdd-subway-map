package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.SectionService;
import subway.util.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(path = "/lines/{id}/sections")
    public SectionResponse createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        return Mapper.toResponse(sectionService.createSection(id, sectionRequest));
    }

    @GetMapping(path = "/lines/{id}/sections")
    public List<SectionResponse> showSections(@PathVariable Long id) {
        return sectionService.findAllSectionsByLineId(id)
                .stream()
                .map(Mapper::toResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping(path = "/lines/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        sectionService.deleteLastSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
