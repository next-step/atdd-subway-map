package subway.station;

import java.util.Objects;
import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    @Column(length = 20, nullable = false)
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public boolean isEqual(Station station) {
        return this.stationId.equals(station.getStationId());
    }

    public boolean isNotEqual(Station station) {
        return !this.stationId.equals(station.getStationId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(stationId, station.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId);
    }
}
