package subway.line;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import subway.station.Station;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;
    @ManyToMany
    private List<Station> stations;

    public Line() {
    }

    public Line(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation) {
        if (!stations.get(getLastElementIndex()).equals(upStation)) {
            throw new MismatchedUpstreamStationException();
        }
        if (stations.contains(downStation)) {
            throw new DownstreamStationIncludedException();
        }
        stations.add(downStation);
    }

    private int getLastElementIndex() {
        return stations.size() - 1;
    }
}
