package subway.line.section;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.section.SectionRequest;
import subway.section.SectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class LineSectionController {
    private final LineSectionService lineSectionService;
    @PostMapping
    public ResponseEntity<SectionResponse> registerSection(@PathVariable Long lineId,
        @RequestBody SectionRequest sectionRequest) {

        LineSection lineSection = lineSectionService.registerLineSection(
            lineId,
            sectionRequest.getUpStationId(),
            sectionRequest.getDownStationId(),
            sectionRequest.getDistance()
        );

        SectionResponse response = SectionResponse.of(lineSection);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> removeSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineSectionService.removeLineSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> searchSections(@PathVariable Long lineId) {
        List<SectionResponse> sections = lineSectionService.findAllSections(lineId).stream()
            .map(SectionResponse::of)
            .collect(Collectors.toList());

        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{lineSectionId}")
    public ResponseEntity<SectionResponse> searchSection(@PathVariable Long lineId, @PathVariable Long lineSectionId) {
        LineSection lineSection = lineSectionService.findLineSectionById(lineSectionId);

        Preconditions.checkState(Objects.equal(lineSection.lineId(), lineId),
            "The section is not in the line");

        SectionResponse response = SectionResponse.of(lineSection);

        return ResponseEntity.ok(response);
    }
}
