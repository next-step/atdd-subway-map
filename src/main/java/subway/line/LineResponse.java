package subway.line;

import subway.station.Station;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = List.of(line.getUpStation(), line.getDownStation());
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

    public List<Station> getStations() {
        return stations;
    }
}
