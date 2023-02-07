package subway.dto;

import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<SectionResponse> stations;
    private final int distance;

    public LineResponse(
            final Long id,
            final String name,
            final String color,
            final List<SectionResponse> stations,
            final int distance
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static LineResponse by(final Line line, final List<SectionResponse> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations,
                line.getDistance()
        );
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

    public List<SectionResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
