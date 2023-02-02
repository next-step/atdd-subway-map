package subway.line;

import subway.exception.StationNotFoundException;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line")
    private List<Station> stations = new ArrayList<>();

    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Long distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        if (upStation == null || downStation == null) {
            throw new StationNotFoundException();
        }
        upStation.changeLine(this);
        downStation.changeLine(this);
        this.stations = List.of(upStation, downStation);
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

    public Station getUpStation() {
        return this.stations.get(0);
    }

    public Station getDownStation() {
        return this.stations.get(this.stations.size() - 1);
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public Long getDistance() {
        return distance;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void breakAllStationRelation() {
        stations.forEach(station -> station.changeLine(null));
    }

    public void addStation(Station station) {
        station.changeLine(this);
        this.stations.add(station);
    }

    public void plusDistance(long distance) {
        this.distance += distance;
    }
}
