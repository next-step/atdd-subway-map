package subway.domain;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.FetchType.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Integer distance;

    protected Section() {}

    public Section(final Long id, final Line line, final Station upStation, final Station downStation, final Integer distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Integer distance) {
        this(id, null, upStation, downStation, distance);
    }

    public Section(final Line line, final Station upStation, final Station downStation, final Integer distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(final Station upStation, final Station downStation, final Integer distance) {
        this(null, null, upStation, downStation, distance);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public boolean matchUpStation(final Station station) {
        return this.upStation.equals(station);
    }

    public boolean matchDownStation(final Station station) {
        return this.downStation.equals(station);
    }

    public void addLine(final Line line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id.equals(section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
