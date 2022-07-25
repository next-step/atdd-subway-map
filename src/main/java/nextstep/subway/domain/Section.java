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

    @Column(name = "is_up_station", nullable = true)
    private Boolean isUpStation;

    @Column(name = "is_down_station", nullable = true)
    private Boolean isDownStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    protected Section() {
    }

    public Section(Line subwayLine, Station station) {
        this.line = subwayLine;
        this.station = station;
    }

    public Station getStation() {
        return station;
    }

    public Line getSubwayLine() {
        return line;
    }
}
