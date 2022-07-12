package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.Arrays;
import java.util.List;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<Long> stations;

    private Long distance;

    public LineResponse(Long id, String name, String color, List<Long> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = Arrays.asList(line.getUpStationId(), line.getDownStationId());
        this.distance = line.getDistance();
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

    public List<Long> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
