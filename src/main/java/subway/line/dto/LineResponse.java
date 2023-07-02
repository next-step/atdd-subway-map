package subway.line.dto;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationOnLineResponse> stationResponses;

    public LineResponse(Long id, String name, String color, List<StationOnLineResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
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

    public List<StationOnLineResponse> getStationResponses() {
        return stationResponses;
    }
}
