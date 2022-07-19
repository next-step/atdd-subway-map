package nextstep.subway.ui;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final StationLineService stationLineService;

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest,
                                                         @PathVariable Long lineId) {
        SectionResponse sectionResponse = stationLineService.createSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create(String.format("/lines/%s/sections", sectionResponse.getId())))
            .body(sectionResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestParam Long stationId,
                                              @PathVariable Long lineId) {
        stationLineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
