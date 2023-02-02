package subway.dto;

import subway.domain.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final int distance;

    public static LineResponse by(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getDistance()
        );
    }

    public LineResponse(
            final Long id,
            final String name,
            final String color,
            final int distance
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }
}
