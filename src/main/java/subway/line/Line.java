package subway.line;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import subway.line.section.Section;
import subway.station.Station;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Stations stations;
    private int distance;

    public Line() {}

    public Line(String name, String color, int distance) {
        this(null, name, color, null, distance);
    }

    public Line(Long id, String name, String color, Stations stations, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void initStations(Station upStation, Station downStation) {
        this.stations = new Stations(this, upStation, downStation);
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
        return this.stations.getStations();
    }

    public List<Section> getSections() {
        return this.stations.getSections();
    }

    public int getDistance() {
        return distance;
    }
}
