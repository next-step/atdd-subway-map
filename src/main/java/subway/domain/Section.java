package subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;
    private Integer distance;

    protected Section () {}

    private Section(Station upStation, Station downStation, Line line, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
    }

    public static Section create(Station upStation, Station downStation, Line line, Integer distance) {
        return new Section(upStation, downStation, line, distance);
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

    public Line getLine() {
        return line;
    }

    public Integer getDistance() {
        return distance;
    }
}
