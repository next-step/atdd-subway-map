package subway.line;

import subway.Station.StationResponse;

import java.util.List;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    private Integer distance;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, Integer distance) {
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
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

}
