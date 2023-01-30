package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.entity.Line;
import subway.line.service.LineService;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LineController {

    private final StationRepository stationRepository;
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> save(@RequestBody LineCreateRequest lineCreateRequest) {
        Station upStation = stationRepository.findById(lineCreateRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(lineCreateRequest.getDownStationId()).orElseThrow();
        Line line = lineService.save(lineCreateRequest.toEntity(upStation, downStation));
        LineResponse response = LineResponse.from(line);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(response);
    }

    @GetMapping("/lines")
    public List<LineResponse> findAll() {
        return lineService.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/lines/{id}")
    public LineResponse findById(@PathVariable Long id) {
        return LineResponse.from(lineService.findById(id));
    }

    @PutMapping("/lines/{id}")
    public void update(@PathVariable Long id, @RequestBody LineUpdateRequest request) {
        request.setLineId(id);
        lineService.update(request);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<SectionResponse> saveSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest request) {
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Section section = lineService.saveSection(lineId, request.toEntity(downStation, upStation));
        return ResponseEntity.created(URI.create("lines/" + request.getLineId() + "/sections")).body(SectionResponse.from(section));
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> delete(@PathVariable Long lineId, Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
