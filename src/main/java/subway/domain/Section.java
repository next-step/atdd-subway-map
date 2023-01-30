package subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Long distance;

    protected Section() {
    }

    public Section(Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean isDownStation(Station station){
        return this.downStation.equals(station);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
