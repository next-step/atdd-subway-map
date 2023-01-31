package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SubwayLineController {

    private final SubwayLineService subwayLineService;

    @PostMapping("/lines")
    public ResponseEntity<SubwayLineResponse> createLine(
        @RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse subwayLineResponse = subwayLineService.saveLine(subwayLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + subwayLineResponse.getId()))
            .body(subwayLineResponse);
    }


}
