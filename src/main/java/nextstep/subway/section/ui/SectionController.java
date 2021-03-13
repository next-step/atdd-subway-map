package nextstep.subway.section.ui;

import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class SectionController {

    @Autowired
    SectionService sectionService;

    @PostMapping("/{lineId}/sections")
    public ResponseEntity createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest){
        SectionResponse sectionResponse = sectionService.createSection(lineId, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{lineId}/sections")
    public ResponseEntity getSectionList(@PathVariable Long lineId){
        return ResponseEntity.ok().body(sectionService.getSectionResponseList(lineId));
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity deleteSection(@PathVariable Long lineId, @RequestParam Long stationId){
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }

}
