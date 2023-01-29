package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.LineLoadService;
import subway.application.service.in.LineCommandUseCase;
import subway.web.request.LineCreateRequest;
import subway.web.request.LineUpdateRequest;
import subway.web.response.CreateLineResponse;
import subway.web.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineController {

    private final LineCommandUseCase lineCommandUseCase;
    private final LineLoadService lineLoadService;

    public LineController(LineCommandUseCase lineCommandUseCase, LineLoadService lineLoadService) {
        this.lineCommandUseCase = lineCommandUseCase;
        this.lineLoadService = lineLoadService;
    }

    @PostMapping("/lines")
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody LineCreateRequest stationRequest) {
        Long createdLineId = lineCommandUseCase.createLine(stationRequest.toDomain());
        CreateLineResponse response = CreateLineResponse.from(lineLoadService.loadLine(createdLineId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> lines(@PathVariable Long lineId) {
        LineResponse line = LineResponse.from(lineLoadService.loadLine(lineId));
        return ResponseEntity.status(HttpStatus.OK).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> lines() {
        List<LineResponse> lineResponses = lineLoadService.loadLines().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(lineResponses);
    }

    @PutMapping("/lines/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineCommandUseCase.updateLine(lineUpdateRequest.toDomain(lineId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineCommandUseCase.deleteLine(lineId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
