package nextstep.subway.ui;

import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // 등록
    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest) {
        SectionResponse response = sectionService.saveSection(id, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/sections" + response.getId())).body(response);
    }


    // 삭제
}
