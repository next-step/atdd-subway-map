package subway.dto;

import lombok.AllArgsConstructor;
import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

@AllArgsConstructor
public class LineResponse {

    private Long id;
    private String name;
    private String color;
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public LineResponse(Line line, List<StationResponse> stations) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        this.stations = stations;
    }
}
