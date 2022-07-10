package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @Embedded
    private Stations stations;

    protected StationLine() {
    }

    public StationLine(String name, String color, int distance, Stations stations) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Stations getStations() {
        return stations;
    }

    public Long getId() {
        return id;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
