package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.service.LineUseCase;
import subway.web.request.LineCreateRequest;
import subway.web.response.CreateLineResponse;

@RestController
public class LineController {

    private final LineUseCase lineUseCase;

    public LineController(LineUseCase lineUseCase) {
        this.lineUseCase = lineUseCase;
    }

    @PostMapping("/lines")
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody LineCreateRequest stationRequest) {
        Long createdLineId = lineUseCase.createLine(stationRequest.toDomain());
        CreateLineResponse response = CreateLineResponse.from(lineUseCase.loadLine(createdLineId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
