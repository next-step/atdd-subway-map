package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.input.SectionLoadUseCase;
import subway.domain.Section;
import subway.web.request.SectionCreateRequest;
import subway.web.response.SectionResponse;

@RestController
public class SectionController {

    private final SectionCommandUseCase sectionCommandUseCase;
    private final SectionLoadUseCase sectionLoadUseCase;

    public SectionController(SectionCommandUseCase sectionCommandUseCase, SectionLoadUseCase sectionLoadUseCase) {
        this.sectionCommandUseCase = sectionCommandUseCase;
        this.sectionLoadUseCase = sectionLoadUseCase;
    }

    @RequestMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest sectionCreateRequest) {
        Long sectionId = sectionCommandUseCase.createSection(sectionCreateRequest.toDto(lineId));
        Section section = sectionLoadUseCase.loadSection(sectionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(SectionResponse.from(section));
    }

    @GetMapping(value = "/sections/{sectionId}")
    public ResponseEntity<SectionResponse> loadSection(@PathVariable Long sectionId) {
        Section section = sectionLoadUseCase.loadSection(sectionId);
        return ResponseEntity.status(HttpStatus.OK).body(SectionResponse.from(section));
    }

}
