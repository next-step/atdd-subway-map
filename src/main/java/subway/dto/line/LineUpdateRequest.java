package subway.dto.line;

import subway.domain.Line;

public class LineUpdateRequest {
    private String name;
    private String color;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line convertToEntity() {
        return new Line(name, color, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
