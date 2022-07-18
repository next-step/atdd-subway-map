package nextstep.subway.line.web;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.application.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable long lineId,
                                              @RequestBody @Valid SectionRequest sectionRequest) {

        sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable long lineId, @RequestParam long stationId) {

        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
