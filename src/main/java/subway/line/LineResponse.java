package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.Station;

import java.util.List;

@AllArgsConstructor
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public LineResponse(Line line, List<Station> stations) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        this.stations = stations;
    }
}
