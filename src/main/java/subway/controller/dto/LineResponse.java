package subway.controller.dto;

import java.util.Set;
import subway.entity.Line;
import subway.entity.Station;

public class LineResponse {

    private final Long id;

    private final String name;

    private final String color;

    private final Set<Station> stations;

    private final Long distance;

    public LineResponse(Long id, String name, String color, Set<Station> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getEndStations().getStations(),
            line.getDistance());
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

    public Set<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
