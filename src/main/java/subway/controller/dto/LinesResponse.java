package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.entity.Line;

public class LinesResponse {

    private final List<LineResponse> lines;

    public LinesResponse(List<LineResponse> lines) {
        this.lines = lines;
    }

    public static LinesResponse from(List<Line> lines) {
        return new LinesResponse(lines.stream()
            .map(LineResponse::from)
            .collect(Collectors.toList()));
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
