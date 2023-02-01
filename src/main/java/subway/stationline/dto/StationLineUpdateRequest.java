package subway.stationline.dto;

import subway.stationline.StationLine;

public class StationLineUpdateRequest {
    private String name;
    private String color;

    public StationLineUpdateRequest() {
    }

    public StationLineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public StationLine convertToEntity() {
        return new StationLine(name, color, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
