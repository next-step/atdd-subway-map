package nextstep.subway.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @Column(name = "index", nullable = true)
    private Long index;

    @Column(name = "distance", nullable = true)
    private Integer distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    protected Section() {
    }

    public Section(Line subwayLine, Station upStation, Station downStation) {
        this.line = subwayLine;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Line getSubwayLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
