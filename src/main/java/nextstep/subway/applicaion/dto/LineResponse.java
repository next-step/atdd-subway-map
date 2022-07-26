package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations = new ArrayList<>();

    private Long distance;

    public LineResponse() {
    }

    public LineResponse(Line line, Station upStation, Station downStation) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations.add(new StationResponse(upStation));
        this.stations.add(new StationResponse(downStation));
        this.distance = line.getDistance();
    }

    public void updateResponse(LineRequest request) {
        this.name = request.getName() != null ? request.getName() : this.name;
        this.color = request.getColor() != null ? request.getColor() : this.color;
        this.distance = request.getDistance() != null ? request.getDistance() : this.distance;
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

    public Long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;

        }
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations, distance);
    }
}
