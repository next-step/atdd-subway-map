package subway.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Stations {

    private static final int STATIONS_MIN = 2;

    @OneToMany(mappedBy = "line")
    private List<Station> stations;

    public Stations() {
    }

    public Stations(final List<Station> stations) {
        if (stations.size() < STATIONS_MIN) {
            throw new IllegalArgumentException();
        }
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void detach() {
        stations.stream()
                .forEach(station -> station.setLine(null));
    }
}
