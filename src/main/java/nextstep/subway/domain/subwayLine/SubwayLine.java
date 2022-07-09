package nextstep.subway.domain.subwayLine;

import nextstep.subway.domain.subwayLineColor.SubwayLineColor;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "subway_line_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "distance")
    private Integer distance;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "color")
    private SubwayLineColor color;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "subwayLine", fetch = LAZY)
    private List<Station> stations;
}
