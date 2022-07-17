package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<Long> stations;

    private Long distance;

    public LineResponse() {
    }

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

    public LineResponse(int id, LineRequest line, LineRequest request) {
        this.id = (long) id;
        this.name = request.getName() != null ? request.getName() : line.getName();
        this.color = request.getColor() != null ? request.getColor() : line.getColor();
        this.distance = request.getDistance() != null ? request.getDistance() : line.getDistance();

        Long upStationId = request.getUpStationId() != null ? request.getUpStationId() : line.getUpStationId();
        Long downStationId = request.getDownStationId() != null ? request.getDownStationId() : line.getDownStationId();
        this.stations = Arrays.asList(upStationId, downStationId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations, distance);
    }
}
