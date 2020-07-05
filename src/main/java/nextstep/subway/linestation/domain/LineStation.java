package nextstep.subway.linestation.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class LineStation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "station_id")
    private Station station;

    @OneToOne
    @JoinColumn(name = "former_station_id")
    private Station formerStation;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Long distance;

    public LineStation() {
    }

    public LineStation(Station station, Station formerStation, Long duration, Long distance) {
        this.station = station;
        this.formerStation = formerStation;
        this.duration = duration;
        this.distance = distance;
    }

    public boolean isSame(LineStation other) {
        return this.equals(other);
    }

    public void updatePreStationTo(Station station) {
        this.formerStation = station;
    }

    public Station getStation() {
        return station;
    }

    public Station getFormerStation() {
        return formerStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineStation that = (LineStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, station, formerStation, duration, distance);
    }
}
