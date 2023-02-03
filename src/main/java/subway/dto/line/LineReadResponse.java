package subway.dto.line;

import subway.domain.Station;
import subway.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class LineReadResponse {
    private Long id;
    private String name;
    private String color;
    private List<LineStationReadResponse> stations = new ArrayList<>();
    private Long upStationId;
    private Long downStationId;

    public LineReadResponse() {
    }

    public LineReadResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();

        for (Station station : stations) {
            this.stations.add(new LineStationReadResponse(station.getId(), station.getName()));
        }

        this.upStationId = line.getUpStationId();
        this.downStationId = line.getDownStationId();
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

    public List<LineStationReadResponse> getStations() {
        return stations;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
