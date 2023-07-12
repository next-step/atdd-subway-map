package subway.station;

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
}
