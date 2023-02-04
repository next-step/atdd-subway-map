package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.SectionService;

import java.net.URI;


@RestController
@RequestMapping("/sections")
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping()
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest) throws Exception{
        SectionResponse line = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + line.getId())).body(line);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteSection(@PathVariable String id) throws Exception{
        sectionService.deleteSection(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
