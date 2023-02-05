package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.input.SectionLoadUseCase;
import subway.domain.Section;
import subway.web.request.SectionCreateRequest;
import subway.web.response.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SectionController {

    private final SectionCommandUseCase sectionCommandUseCase;
    private final SectionLoadUseCase sectionLoadUseCase;

    public SectionController(SectionCommandUseCase sectionCommandUseCase, SectionLoadUseCase sectionLoadUseCase) {
        this.sectionCommandUseCase = sectionCommandUseCase;
        this.sectionLoadUseCase = sectionLoadUseCase;
    }

    @GetMapping(value = "lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> loadSections(@PathVariable Long lineId) {
        List<Section> sections = sectionLoadUseCase.loadLineSection(lineId);
        List<SectionResponse> sectionResponses = sections.stream().map(SectionResponse::from).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(sectionResponses);
    }

    @PostMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest sectionCreateRequest) {
        Long sectionId = sectionCommandUseCase.createSection(sectionCreateRequest.toDto(lineId));
        Section section = sectionLoadUseCase.loadSection(sectionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(SectionResponse.from(section));
    }

    @DeleteMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @RequestParam Long sectionId) {
        sectionCommandUseCase.deleteLineSection(lineId, sectionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/sections/{sectionId}")
    public ResponseEntity<SectionResponse> loadSection(@PathVariable Long sectionId) {
        Section section = sectionLoadUseCase.loadSection(sectionId);
        return ResponseEntity.status(HttpStatus.OK).body(SectionResponse.from(section));
    }

}
