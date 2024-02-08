package subway.section.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.request.SectionCreateRequest;
import subway.section.response.SectionResponse;
import subway.section.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{id}/sections")
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping()
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @Valid @RequestBody SectionCreateRequest sectionCreateRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(id, sectionCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + sectionResponse.getId() + "/sections")).body(sectionResponse);
    }
}
