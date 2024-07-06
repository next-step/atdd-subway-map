package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.entity.Line;
import subway.domain.service.LineService;
import subway.controller.dto.CreateLineRequest;
import subway.controller.dto.CreateLineResponse;
import subway.controller.dto.LineResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping()
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody CreateLineRequest request) {
        Long id = lineService.createLine(request.toCommand());

        return ResponseEntity.created(URI.create("/lines/" + id)).body(
                new CreateLineResponse(
                        request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()
                )
        );
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLines() {
        List<Line> lines = lineService.getLines();
        List<LineResponse> response = lines.stream().map((line) -> new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                new ArrayList<>()
        )).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }
}
