package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.SectionCreateRequest;
import subway.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable Long lineId,
            @Valid @RequestBody SectionCreateRequest request) {
        Long sectionId = sectionService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/stations/" + sectionId)).build();
    }
}
