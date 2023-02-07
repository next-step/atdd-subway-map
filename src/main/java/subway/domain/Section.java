package subway.domain;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    private Long distance;

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

    public Long getDistance() {
        return distance;
    }

    protected Section() {

    }
    public Section(Line line, Station upStation, Station downStation, Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean contains(Station downStation, Station upStation) {
        return (this.downStation == downStation && this.upStation == upStation);
    }
}
