package subway.domain;

import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Distance distance;

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Line line;

    protected Section() {
    }

    public Section(final int distance, final Station upStation, final Station downStation, final Line line) {
        this.distance = new Distance(distance);
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        line.updateDownStation(downStation);
    }

    public Long getId() {
        return id;
    }

    public Distance getDistance() {
        return distance;
    }

    public Optional<Station> getUpStation() {
        return Optional.ofNullable(upStation);
    }

    public Optional<Station> getDownStation() {
        return Optional.ofNullable(downStation);
    }

    public Optional<Line> getLine() {
        return Optional.ofNullable(line);
    }

    public void detachLine() {
        this.line = null;
    }

    public boolean isEqualDownStation(final Station station) {
        return this.downStation.equals(station);
    }
}
