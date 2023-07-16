package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.subway.adapter.in.web.mapper.SubwayLineDetailQueryMapper;
import subway.subway.application.in.query.SubwayLineDetailQuery;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

@RestController
public class SubwayLineDetailQueryController {

    private final SubwayLineDetailQuery subwayLineDetailQuery;
    private final SubwayLineDetailQueryMapper mapper;

    public SubwayLineDetailQueryController(SubwayLineDetailQuery subwayLineDetailQuery, SubwayLineDetailQueryMapper mapper) {
        this.subwayLineDetailQuery = subwayLineDetailQuery;
        this.mapper = mapper;
    }

    @GetMapping("/subway-lines/{id}")
    ResponseEntity<SubwayLineResponse> findOne(@PathVariable Long id) {
        SubwayLineDetailQuery.Command command = mapper.mapFrom(id);
        SubwayLineResponse response = subwayLineDetailQuery.findOne(command);
        return ResponseEntity.ok().body(response);
    }
}
