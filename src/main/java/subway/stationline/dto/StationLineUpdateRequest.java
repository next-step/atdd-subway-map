package subway.stationline.dto;

public class StationLineUpdateRequest {
    private String name;
    private String color;

    public StationLineUpdateRequest() {
    }

    public StationLineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
