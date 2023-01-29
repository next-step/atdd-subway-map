package subway.line;

import subway.station.Station;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;

    private final List<Station> stations;

    private LineResponse(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(line.getUpStation(), line.getDownStation())
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

    public List<Station> getStations() {
        return stations;
    }
}
