package subway.dto;

import subway.domain.Line;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private Long distance;
    private List<StationResponse> stations;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public LineResponse(Line line, List<StationResponse> stations) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        distance = line.getTotalDistance();
        this.stations = stations;
    }
}
