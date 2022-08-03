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

    @Column(name = "distance", nullable = true)
    private Integer distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id", nullable = true)
    private Line line;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Section(Line line, Station upStation, Station downStation, Integer distance) {
        this.line = line;
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

    public Integer getDistance() {
        return distance;
    }
}
