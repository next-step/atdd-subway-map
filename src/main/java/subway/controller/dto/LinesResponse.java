package subway.controller.dto;

import java.util.List;
import subway.entity.Line;

public class LinesResponse {

    private final List<Line> lines;

    public LinesResponse(List<Line> lines) {
        this.lines = lines;
    }

    public static LinesResponse from(List<Line> lines) {
        return new LinesResponse(lines);
    }

    public List<Line> getLines() {
        return lines;
    }
}
