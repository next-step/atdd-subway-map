package subway.line.presentation;

import subway.line.domain.Line;
import subway.line.domain.Sections;
import subway.station.domain.Station;

import java.util.List;

public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<Station> stations;

    public LineResponse() {
    }

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = line.getStations();
    }

    public LineResponse(Long id, String name, String color, List<Station> stations) {
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

    public List<Station> getStations() {
        return stations;
    }
}
