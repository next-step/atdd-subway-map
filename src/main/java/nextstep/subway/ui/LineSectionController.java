package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionApiService;
import nextstep.subway.ui.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LineSectionController {

    private final SectionApiService sectionApiService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable Long lineId,
            @RequestBody @Valid SectionRequest sectionRequest) {
        sectionApiService.createSection(lineId, sectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long lineId,
            @RequestParam Long stationId
    ) {
        sectionApiService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }

}
