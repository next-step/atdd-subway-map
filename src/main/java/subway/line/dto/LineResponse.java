package subway.line.dto;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private StationsDto stations;

    public LineResponse(Long id, String name, String color, StationsDto stations) {
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

    public StationsDto getStations() {
        return stations;
    }
}
