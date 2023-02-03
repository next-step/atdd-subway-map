package subway.line.dto;

import subway.station.Station;
import subway.line.Line;

import java.util.ArrayList;
import java.util.List;

public class LineCreateResponse {
    private Long id;
    private String name;
    private String color;
    private List<LineStationCreateResponse> stations = new ArrayList<>();

    public LineCreateResponse() {
    }

    public LineCreateResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();

        for (Station station : stations) {
            this.stations.add(new LineStationCreateResponse(station.getId(), station.getName()));
        }
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

    public List<LineStationCreateResponse> getStations() {
        return stations;
    }
}
