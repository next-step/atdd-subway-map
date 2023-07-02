package subway.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.exception.EndStationNotValidStationCountException;
import subway.exception.LineNotEstablishedBySameEndStationException;

@Entity
public class EndStations {

    public static final int VALID_END_STATIONS_COUNT = 2;
    public static final int SAME_END_STATION_CRITERIA = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private Set<Station> stations;

    public EndStations() {}

    public EndStations(Set<Station> stations) {
        this.stations = valid(stations);
    }

    public EndStations clone() {
        return new EndStations(stations);
    }

    public static EndStations of(Set<Station> endStations) {
        return new EndStations(endStations);
    }

    public Set<Station> getStations() {
        return stations;
    }

    private Set<Station> valid(Set<Station> stations) {
        if (isSameEndStation(stations)) {
            throw new LineNotEstablishedBySameEndStationException("노선의 두 종착역은 동일할 수 없습니다.");
        }
        if (isCountNotValid(stations)) {
            throw new EndStationNotValidStationCountException(
                String.format("노선의 종착역은 항상 2개의 역으로만 구성가능합니다: %d 개", stations.size()));
        }
        return stations;
    }

    private boolean isSameEndStation(Set<Station> stations) {
        return stations.size() == SAME_END_STATION_CRITERIA;
    }

    private boolean isCountNotValid(Set<Station> stations) {
        return stations.size() != VALID_END_STATIONS_COUNT;
    }

}
