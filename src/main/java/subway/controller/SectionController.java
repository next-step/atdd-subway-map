package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.SectionRequest;
import subway.service.LineService;

import java.net.URI;

@RestController
public class SectionController {

    private LineService lineService;

    public SectionController(LineService lineService){
        this.lineService = lineService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResposne> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest){
        SectionResponse section = lineService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId+"/sections").body(section);
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId){
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
