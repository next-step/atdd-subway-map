package subway.line;

import subway.subway.Station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;

    @OneToMany
    private List<Station> stationList = new ArrayList<>();
    private Long distance;

    public Line(Long id, String name, String color, List<Station> stationList, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationList = stationList;
        this.distance = distance;
    }
    public Line (String name, String color, List<Station> stationList, Long distance) {
        this.name = name;
        this.color = color;
        this.stationList = stationList;
        this.distance = distance;
    }

    public Line() {
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

    public List<Station> getStationList() {
        return stationList;
    }

    public Long getDistance() {
        return distance;
    }
}
