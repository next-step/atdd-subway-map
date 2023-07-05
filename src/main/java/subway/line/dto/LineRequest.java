package subway.line.dto;

import subway.line.domain.Line;

public class LineRequest {

    private String name;
    private String color;

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return color;
    }

    public Line toEntity() {
        return new Line(name, color);
    }


}
