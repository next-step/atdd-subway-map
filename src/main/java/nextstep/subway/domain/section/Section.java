package nextstep.subway.domain.section;

import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne()
    @JoinColumn(name = "up_station_id", referencedColumnName = "station_id")
    private Station upStation;

    @OneToOne()
    @JoinColumn(name = "down_station_id", referencedColumnName = "station_id")
    private Station downStation;

    private Long distance;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, Long distance) {
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

    public Long getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }
}
