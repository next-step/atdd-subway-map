package subway.domain.response;

import java.util.List;

public class SubwayLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public SubwayLineResponse(Long id, String name, String color, List<StationResponse> stations) {
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
