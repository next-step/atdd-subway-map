package subway.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Stations stations = new Stations();

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.stations = new Stations(List.of(upStation, downStation), distance);
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

    public Long getUpStationId() {
        return stations.getUpStationId();
    }

    public Long getDownStationId() {
        return stations.getDownStationId();
    }

    public void modifyLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void createLineSection(Station upStation, Station downStation, int distance) {
        stations.createLineSection(upStation, downStation, distance);
    }

    public void deleteLineSection(Station station) {
        stations.deleteLineSection(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(stations, line.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }
}
