package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.SectionService;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(CreateSectionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
