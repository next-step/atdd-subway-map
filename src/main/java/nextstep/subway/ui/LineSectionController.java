package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.ui.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LineSectionController {

    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable Long lineId,
            @RequestBody @Valid SectionRequest sectionRequest) {
        sectionService.createSection(lineId, sectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long lineId,
            @RequestParam Long stationId
    ) {
        sectionService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }

}
