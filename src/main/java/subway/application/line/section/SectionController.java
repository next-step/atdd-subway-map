package subway.application.line.section;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.LineService;

@RestController
@RequestMapping("/lines/{id}")
@RequiredArgsConstructor
public class SectionController {

    private final LineService lineService;

    @PostMapping("/sections")
    public ResponseEntity<Void> registSection(
        @RequestBody SectionRequest request,
        @PathVariable Long id
    ) {
        lineService.registSection(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(
        @RequestParam String stationId,
        @PathVariable Long id
    ) {
        lineService.deleteSection(id, Long.parseLong(stationId));
        return ResponseEntity.noContent().build();
    }
}
