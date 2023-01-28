package subway.route;

import subway.StationResponse;

import java.util.List;

public class RouteResponse {
    private Long id;

    private String name;
    private String color;

    private List<StationResponse> stations;

    public RouteResponse(Long id, String name, String color, List<StationResponse> stations) {
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
