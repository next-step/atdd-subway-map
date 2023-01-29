package subway.dto;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Long distance;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, Long distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }
}
