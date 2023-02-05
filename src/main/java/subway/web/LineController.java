package subway.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.input.LineCommandUseCase;
import subway.application.service.input.LineLoadUseCase;
import subway.web.request.LineCreateRequest;
import subway.web.request.LineUpdateRequest;
import subway.web.response.LineLoadDtoResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineController {

    private final LineCommandUseCase lineCommandUseCase;
    private final LineLoadUseCase lineLoadUseCase;

    public LineController(LineCommandUseCase lineCommandUseCase, LineLoadUseCase lineLoadService) {
        this.lineCommandUseCase = lineCommandUseCase;
        this.lineLoadUseCase = lineLoadService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineLoadDtoResponse> createLine(@RequestBody LineCreateRequest stationRequest) {
        Long createdLineId = lineCommandUseCase.createLine(stationRequest.toDomain());
        LineLoadDtoResponse response = LineLoadDtoResponse.from(lineLoadUseCase.loadLineDto(createdLineId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineLoadDtoResponse> lines(@PathVariable Long lineId) {
        LineLoadDtoResponse line = LineLoadDtoResponse.from(lineLoadUseCase.loadLineDto(lineId));
        return ResponseEntity.status(HttpStatus.OK).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineLoadDtoResponse>> lines() {
        List<LineLoadDtoResponse> lineLoadDtoResponse = lineLoadUseCase.loadLineDtos().stream()
            .map(LineLoadDtoResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(lineLoadDtoResponse);
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
