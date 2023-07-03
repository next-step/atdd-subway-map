package subway.line.repository;

import subway.station.repository.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line")
    private List<Station> stations = new ArrayList<>();

    public Line() {}
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addStation(Station station) {
        stations.add(station);
        station.updateLine(this);
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
