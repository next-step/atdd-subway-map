package subway.station.domain.line;

import subway.station.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String color;

    @Column
    private Long upStationId;

    @Column
    private Long downStationId;

    @Column
    private Long distance;

    @OneToMany
    @Column
    private List<Station> stations;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.stations = stations;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
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

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
