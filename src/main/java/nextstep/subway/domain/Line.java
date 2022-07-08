package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    @Embedded
    private Distance distance;
    @Embedded
    private Stations stations;

    protected Line() {
    }

    public Line(String name, String color, long distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = new Distance(distance);
        this.stations = new Stations(upStation, downStation);
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

    public Distance getDistance() {
        return distance;
    }

    public Stations getStations() {
        return stations;
    }
}
