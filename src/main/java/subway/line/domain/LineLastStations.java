package subway.line.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import subway.station.domain.Station;

@Embeddable
public class LineLastStations {
    @ManyToOne
    private Station upLastStation;

    @ManyToOne
    private Station downLastStation;

    protected LineLastStations() {}

    public LineLastStations(Station upLastStation, Station downLastStation) {
        if (upLastStation.equals(downLastStation)) {
            throw new IllegalArgumentException();
        }

        this.upLastStation = upLastStation;
        this.downLastStation = downLastStation;
    }

    public static LineLastStations createLineLastStation(List<Station> stations) {
        if (stations.size() != 2) {
            throw new IllegalArgumentException();
        }

        return new LineLastStations(stations.get(0), stations.get(1));
    }

    public List<Station> getStations() {
        return Arrays.asList(upLastStation, downLastStation);
    }

    public Station getUpLastStation() {
        return upLastStation;
    }

    public Station getDownLastStation() {
        return downLastStation;
    }
}
