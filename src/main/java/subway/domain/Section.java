package subway.domain;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    protected Section() {
    }

    public Section(Line line,Station upStation, Station downStation, Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean equalsDownStation(Section section){
        return this.downStation.equals(section.getUpStation());
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

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Section section = (Section) o;
        return id != null && Objects.equals(id, section.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean hasStation(Station station) {
        return this.downStation.equals(station) || this.upStation.equals(station);
    }

    public boolean isUpStation(Station station){
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station){
        return this.downStation.equals(station);
    }
}
