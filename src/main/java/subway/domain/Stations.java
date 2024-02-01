package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Stations {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Station> stations = new ArrayList<>();

    public static Stations of(Station upStation, Station downStation) {
        Stations stations = new Stations();
        stations.add(upStation);
        stations.add(downStation);
        return stations;

    }

    public List<Station> getStations() {
        return stations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public void addLine(Line line) {
        stations.forEach(station -> station.updateLine(line));
    }
}
