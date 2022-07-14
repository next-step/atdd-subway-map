package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Line line, List<StationResponse> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
