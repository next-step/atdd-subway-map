package subway.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Stations {

    private static final int STATIONS_MIN = 2;

    @OneToMany(mappedBy = "line")
    private List<Station> stations;

    protected Stations() {
    }

    public Stations(final List<Station> stations) {
        if (stations.size() < STATIONS_MIN) {
            throw new IllegalArgumentException("노선에는 최소 2개 이상의 역이 존재해야 합니다.");
        }
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void detach() {
        stations.forEach(station -> station.setLine(null));
    }
}
