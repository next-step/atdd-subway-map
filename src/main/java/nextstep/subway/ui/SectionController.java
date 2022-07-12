package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.SectionService;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import nextstep.subway.ui.dto.section.SectionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId, CreateSectionRequest request){
        SectionResponse sectionResponse = sectionService.createSection(lineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponse);
    }
}
