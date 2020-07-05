package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineStationService;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/lines/{id}/stations")
public class LineStationController {
    private final StationRepository stationRepository;
    private final LineStationService lineStationService;

    public LineStationController(final StationRepository stationRepository, final LineStationService lineStationService) {
        this.stationRepository = stationRepository;
        this.lineStationService = lineStationService;
    }

    @PostMapping
    public ResponseEntity<LineStationResponse> addStation(@PathVariable Long id,
                                                          @RequestBody LineStationRequest lineStationRequest) {
        if (!this.stationRepository.existsById(lineStationRequest.getStationId())) {
            throw new EntityNotFoundException();
        }

        if (Objects.nonNull(lineStationRequest.getPreStationId())) {
            if (!this.stationRepository.existsById(lineStationRequest.getPreStationId())) {
                throw new EntityNotFoundException();
            }
        }

        LineStation lineStation = this.lineStationService.addStation(id, lineStationRequest);
        final Station station = this.stationRepository.findById(lineStation.getStationId()).orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.created(URI.create("/lines/" + id + "/stations")).body(LineStationResponse.of(lineStation, station));
    }
}
