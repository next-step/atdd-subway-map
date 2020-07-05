package nextstep.subway.linestation.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class LineStation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "station_id")
    private Station station;

    @OneToOne
    @JoinColumn(name = "former_station_id")
    private Station formerStation;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Long distance;
}
