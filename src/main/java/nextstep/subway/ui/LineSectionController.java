package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionDto;
import nextstep.subway.ui.dto.SectionRequest;
import nextstep.subway.ui.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LineSectionController {

    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable Long lineId,
            @RequestBody @Valid SectionRequest sectionRequest) {
        SectionDto sectionDto = sectionService.createSection(lineId, sectionRequest);
        SectionResponse response = SectionResponse.of(sectionDto);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(response);
    }

    @DeleteMapping("/lines/{lineId}/sections/{sectionId}")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long lineId,
            @PathVariable Long sectionId
    ) {
        sectionService.deleteSection(lineId, sectionId);

        return ResponseEntity.noContent().build();
    }

}
