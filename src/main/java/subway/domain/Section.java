package subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Distance distance;

    @ManyToOne(optional = false)
    private Station upStation;

    @ManyToOne(optional = false)
    private Station downStation;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Line line;

    protected Section() {
    }

    public Section(final int distance, final Station upStation, final Station downStation, final Line line) {
        this.distance = new Distance(distance);
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Distance getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Line getLine() {
        return line;
    }

    public boolean contain(final Station station) {
        return isEqualDownStation(station) || isEqualUpStation(station);
    }

    public boolean isEqualDownStation(final Station station) {
        return this.downStation.equals(station);
    }

    public boolean isEqualUpStation(final Station station) {
        return this.upStation.equals(station);
    }
}
