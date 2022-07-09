package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Station> stations = new ArrayList<>();

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, int distance, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.stations.addAll(stations);
    }

    public Long getId() {
        return id;
    }

    public List<Station> getStations() {
        return stations;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
