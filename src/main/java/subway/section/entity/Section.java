package subway.section.entity;

import subway.station.entity.Station;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @NotNull
    private Long distance;

    @NotNull
    private Long position;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Long distance, Long position) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.position = position;
    }

    public static Section of(Station upStation, Station downStation, Long distance, Long position) {
        return new Section(upStation, downStation, distance, position);
    }


    public Long getId() { return id; }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public Long getDistance() {
        return this.distance;
    }

    public Long getPosition() {
        return position;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }
}
