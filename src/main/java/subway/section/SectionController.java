package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.SectionCreateRequest;
import subway.line.repository.LineRepository;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.service.SectionService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    private final LineRepository lineRepository;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> saveSection(@PathVariable long lineId, @RequestBody SectionCreateRequest request) {
        request.setLineId(lineId);
        Section saved = sectionService.save(request);
        return ResponseEntity.created(URI.create("lines/" + request.getLineId() + "/sections")).body(SectionResponse.from(saved));
    }
}
