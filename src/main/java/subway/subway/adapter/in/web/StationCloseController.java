package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.subway.application.in.StationCloseUsecase;
import subway.subway.domain.Station;

@RestController
class StationCloseController {
    private final StationCloseUsecase stationCloseUsecase;

    StationCloseController(StationCloseUsecase stationCloseUsecase) {
        this.stationCloseUsecase = stationCloseUsecase;
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationCloseUsecase.closeStation(new StationCloseUsecase.Command(new Station.Id(id)));
        return ResponseEntity.noContent().build();
    }
}
