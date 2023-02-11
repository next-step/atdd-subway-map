package subway.line;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lines/{lineId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public void addSection(
            @PathVariable final Long lineId,
            @RequestBody final SectionAddRequest request
    ) {

        sectionService.addSection(lineId, request);
    }
}
