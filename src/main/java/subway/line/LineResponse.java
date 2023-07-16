package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    List<StationResponse> stations;

    public LineResponse() {
    }
    public LineResponse(Line line, Station upStation, Station downStation) {
        this.name = line.getName();
        this.color = line.getColor();
        this.id = line.getId();
        this.stations = new ArrayList<>();
        stations.add(new StationResponse(upStation.getId(), upStation.getName()));
        stations.add(new StationResponse(downStation.getId(), downStation.getName()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }
}
