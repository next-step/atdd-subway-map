package subway.section;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.service.SectionService;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final StationRepository stationRepository;
    private final SectionService sectionService;

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> saveSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest request) {
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Section section = sectionService.save(lineId, request.toEntity(downStation, upStation));
        return ResponseEntity.created(URI.create("lines/" + request.getLineId() + "/sections")).body(SectionResponse.from(section));
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> delete(@PathVariable Long lineId, Long stationId) {
        sectionService.delete(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
