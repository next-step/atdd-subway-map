package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.StationLineService;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class StationLineController {
    private final StationLineService stationLineService;

    @PostMapping("lines")
    public ResponseEntity<StationLineResponse> cretaeStationLine(@RequestBody StationLineRequest stationLineRequest) {
        StationLineResponse stationLineResponse = stationLineService.createStationLine(stationLineRequest);
        return ResponseEntity.created(URI.create("/lines" + stationLineResponse.getId())).body(stationLineResponse);
    }

}
