package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.subway.application.in.query.SubwayLineListQuery;
import subway.subway.application.query.SubwayLineResponse;

import java.util.List;

@RestController
class SubwayLineListQueryController {

    private final SubwayLineListQuery subwayLineListQuery;

    public SubwayLineListQueryController(SubwayLineListQuery subwayLineListQuery) {
        this.subwayLineListQuery = subwayLineListQuery;
    }

    @GetMapping("/subway-lines")
    ResponseEntity<List<SubwayLineResponse>> findAll() {
        List<SubwayLineResponse> subwayLineResponses = subwayLineListQuery.findAll();
        return ResponseEntity.ok().body(subwayLineResponses);
    }
}
