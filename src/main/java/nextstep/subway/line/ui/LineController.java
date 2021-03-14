package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;
    private StationService stationService;

    public LineController(final LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineResponse>> showLines(){
        return ResponseEntity.ok().body(lineService.findAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id){
        return ResponseEntity.ok().body(lineService.findStationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity modifyLine(@RequestBody LineRequest lineRequest, @PathVariable Long id){
        lineService.modifyLine(lineRequest, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id){
        lineService.deleteLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> registerLineSection(@PathVariable Long lineId, @RequestBody LineRequest lineRequest){
        // 구간에 등록될 라인 정보 조회 : 서비스간의 참조를 컨트롤러에서 처리
        Station upStation = stationService.selectStationById(lineRequest.getUpStationId());
        Station downStation = stationService.selectStationById(lineRequest.getDownStationId());
        // 지하철 노선에 구간을 등록
        LineResponse response = lineService.registerLineSection(lineId, lineRequest, upStation, downStation);
        return ResponseEntity.created(URI.create("lines/"+lineId+"/sections")).body(response); // 201, 300의 경우 Location값을 준다.
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<LineResponse> deleteLineSection(@PathVariable Long lineId, @RequestParam("stationId") Long stationId){
        LineResponse response = lineService.deleteLineSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
