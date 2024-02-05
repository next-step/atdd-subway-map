package subway.line;

import subway.station.StationsResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private StationsResponse stations;

    public LineResponse(Long id, String name, String color, StationsResponse stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public String getName() {
        return name;
    }
}
