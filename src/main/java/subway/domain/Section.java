package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public boolean isContainStation(String stationName) {
        return getDownStation().getName().equals(stationName) || getUpStation().getName().equals(stationName);
    }

    public boolean equalsUpStation(Station station) {
        return upStation.getName().equals(station.getName());
    }

    public boolean equalsDownStation(Station station) {
        return downStation.getName().equals(station.getName());
    }
}
