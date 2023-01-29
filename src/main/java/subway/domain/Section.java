package subway.domain;

import javax.persistence.*;

@Entity
@Table(name = "SECTION")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "LINE_ID")
    private Line line;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;
    private Integer distance;

    public Section(final Line line, final Station upStation, final Station downStation, final int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    protected Section() {
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
}
