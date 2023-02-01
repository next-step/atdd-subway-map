package subway.dto;

import java.util.Collections;
import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final int distance;

    public static LineResponse by(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                StationResponse.by(line.getStations()),
                line.getDistance()
        );
    }

    public LineResponse(
            final Long id,
            final String name,
            final String color,
            final List<StationResponse> stations,
            final int distance
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
