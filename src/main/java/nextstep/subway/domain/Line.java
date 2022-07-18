package nextstep.subway.domain;

import nextstep.subway.exception.StationNotRegisteredException;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @OneToMany
    @JoinColumn(name = "line_id")
    private List<Station> stations;

    protected Line() {
    }

    public Line(String name, String color, List<Station> stations, int distance) {
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public Line changeBy(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
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

    public Line addSection(Station newStation, int distance) {
        stations.add(newStation);
        this.distance = distance;
        return this;
    }

    public Station lastStation() {
        return stations.stream().max(Comparator.comparing(Station::getId))
                .orElseThrow(() -> new StationNotRegisteredException("노선에 등록된 역이 없습니다."));
    }

    public boolean hasStation(long id) {
        return stations.stream().anyMatch(station -> station.equalsId(id));
    }
}
