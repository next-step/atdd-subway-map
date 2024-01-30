package subway.line.create;

import java.util.List;

public class LineCreatedResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<LineCreatedStationResponse> stations;

    public LineCreatedResponse(Long id, String name, String color, List<LineCreatedStationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<LineCreatedStationResponse> getStations() {
        return stations;
    }
}
