package subway.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Embedded
    private Stations stations;

    @Embedded
    private Distance distance;

    public Line() {
    }

    public Line(final String name, final String color, final List<Station> stations, final int distance) {
        this.name = name;
        this.color = color;
        this.stations = new Stations(stations);
        this.distance = new Distance(distance);
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
        return stations.getStations();
    }

    public int getDistance() {
        return distance.getValue();
    }
}
