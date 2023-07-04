package subway.vo;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public List<StationResponse> getStations() { return stations; }

    public void setStations(List<StationResponse> stations) { this.stations = stations; }
}
