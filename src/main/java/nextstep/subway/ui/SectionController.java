package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.SectionService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lines")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest request,
                                                         @PathVariable("id") Long lineId) {
        SectionResponse section = sectionService.saveSection(request, lineId);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + section.getId())).body(section);
    }
 }
