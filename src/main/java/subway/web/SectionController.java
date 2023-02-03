package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.service.input.SectionCommandUseCase;
import subway.web.request.SectionCreateRequest;

@RestController
public class SectionController {

    private final SectionCommandUseCase sectionCommandUseCase;

    public SectionController(SectionCommandUseCase sectionCommandUseCase) {
        this.sectionCommandUseCase = sectionCommandUseCase;
    }

    @RequestMapping(value = "/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest sectionCreateRequest) {
        sectionCommandUseCase.createSection(sectionCreateRequest.toDto(lineId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
