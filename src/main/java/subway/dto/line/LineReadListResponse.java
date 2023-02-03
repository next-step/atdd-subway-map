package subway.dto.line;

import subway.domain.Station;
import subway.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class LineReadListResponse {
    private Long id;
    private String name;
    private String color;
    private List<LineStationReadListResponse> stations = new ArrayList<>();
    private Long upStationId;
    private Long downStationId;

    public LineReadListResponse() {
    }

    public LineReadListResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();

        for (Station station : stations) {
            this.stations.add(new LineStationReadListResponse(station.getId(), station.getName()));
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

    public List<LineStationReadListResponse> getStations() {
        return stations;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
