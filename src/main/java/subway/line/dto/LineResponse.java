package subway.line.dto;

import subway.line.domain.Line;

public class LineResponse {

    private final long id;
    private final String name;
    private final String color;

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getColor()
    {
        return color;
    }

    public LineResponse(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(Line line) {
        this(line.getId(), line.getName(), line.getColor());
    }
}
