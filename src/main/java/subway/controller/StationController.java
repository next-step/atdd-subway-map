package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.controller.dto.StationRequest;
import subway.domain.command.StationCommander;
import subway.domain.view.StationView;
import subway.domain.query.StationReader;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StationController {
    private final StationCommander stationCommander;
    private final StationReader stationReader;

    @PostMapping("/stations")
    public ResponseEntity<StationView.Main> createStation(@RequestBody StationRequest stationRequest) {
        Long id = stationCommander.createStation(stationRequest.getName());
        StationView.Main view = stationReader.getOneById(id);
        return ResponseEntity.created(URI.create("/stations/" + id)).body(view);
    }

    @GetMapping(value = "/stations")
    public ResponseEntity<List<StationView.Main>> showStations() {
        List<StationView.Main> views = stationReader.getAll();
        return ResponseEntity.ok().body(views);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationCommander.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
