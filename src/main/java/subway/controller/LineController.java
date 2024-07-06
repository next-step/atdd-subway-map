package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.repository.StationRepository;
import subway.domain.service.LineService;
import subway.controller.dto.CreateLineRequest;
import subway.controller.dto.CreateLineResponse;
import subway.controller.dto.LineResponse;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;
    private final StationRepository stationRepository;

    @PostMapping()
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
        Long id = lineService.createLine(request.toCommand());

        return ResponseEntity.created(URI.create("/lines/" + id)).body(
                new CreateLineResponse(
                        request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()
                )
        );
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLines() {
        List<Line> lines = lineService.getLines();
        List<Station> stations = stationRepository.findAllById(lines.stream()
                .flatMap(line -> Arrays.stream(new Long[] { line.getUpStationId(), line.getDownStationId() }))
                .collect(Collectors.toSet()));

        return ResponseEntity.ok().body(joinStations(lines, stations));
    }

    private List<LineResponse> joinStations(List<Line> lines, List<Station> stations) {
        Map<Long, Station> mapStation = new HashMap<>();
        stations.forEach((station -> mapStation.putIfAbsent(station.getId(), station)));
        return lines.stream().map(line -> {
            List<Station> upDownStation = new ArrayList<>();
            Station upStation = mapStation.get(line.getUpStationId());
            Station downStation = mapStation.get(line.getDownStationId());
            if (upStation != null) {
                upDownStation.add(upStation);
            }

            if (downStation != null) {
                upDownStation.add(downStation);
            }

            return new LineResponse(
                    line.getId(),
                    line.getName(),
                    line.getColor(),
                    upDownStation
            );
        }).collect(Collectors.toList());
    }
}
