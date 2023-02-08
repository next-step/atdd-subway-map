package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.color.CreateColorRequest;
import subway.dto.color.CreateColorResponse;
import subway.dto.color.ReadColorResponse;
import subway.service.ColorService;

import java.net.URI;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @PostMapping("")
    public ResponseEntity<CreateColorResponse> createLine(@RequestBody CreateColorRequest request) {
        CreateColorResponse response = colorService.createColor(request);
        return ResponseEntity
                .created(URI.create("/color/" + response.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadColorResponse> readColor(@PathVariable Long id) {
        return ResponseEntity
                .ok(colorService.readColor(id));
    }

}
