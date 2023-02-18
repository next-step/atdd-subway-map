package subway.line.section;

import java.util.List;
import java.util.Objects;
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
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundLineSectionException;
import subway.line.LineResponse;
import subway.line.LineService;
import subway.section.SectionRequest;
import subway.section.SectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class LineSectionController {

    private final LineService lineService;


    @PostMapping
    public ResponseEntity<SectionResponse> registerSection(@PathVariable Long lineId,
        @RequestBody SectionRequest sectionRequest) {

        SectionResponse response = lineService.addLineSection(lineId, sectionRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> removeSection(@PathVariable Long lineId, @RequestParam Long downStationId) {
        lineService.removeLineSection(lineId, downStationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> searchSections(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.findById(lineId)
            .orElseThrow(NotFoundLineException::new);

        List<SectionResponse> sections = lineResponse.getSections();

        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{upStationId}/{downStationId}")
    public ResponseEntity<SectionResponse> searchSection(@PathVariable Long lineId, @PathVariable Long downStationId, @PathVariable Long upStationId) {
        LineResponse lineResponse = lineService.findById(lineId)
            .orElseThrow(NotFoundLineException::new);

        SectionResponse sectionResponse = lineResponse.getSections().stream()
            .filter(it -> Objects.equals(it.getDownStationId(), downStationId))
            .filter(it -> Objects.equals(it.getUpStationId(), upStationId))
            .findFirst().orElseThrow(NotFoundLineSectionException::new);

        return ResponseEntity.ok(sectionResponse);
    }
}
