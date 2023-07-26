package subway.dto;

import java.util.List;

public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationDto> stations;

    private Long distance;

    public LineResponse(Long id, String name, String color, List<StationDto> stations, Long distance) {
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

    public List<StationDto> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
