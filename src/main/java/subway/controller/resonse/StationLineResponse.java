package subway.controller.resonse;

import java.util.List;

public class StationLineResponse {

    private long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public StationLineResponse(long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public long getId() {
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
