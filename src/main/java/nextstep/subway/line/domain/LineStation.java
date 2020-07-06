package nextstep.subway.line.domain;

import javax.persistence.*;

@Entity
@Table
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long stationId;
    private Long preStationId;
    private Integer distance;
    private Integer duration;

    public LineStation() {
    }

    public LineStation(Long stationId, Long preStationId, Integer distance, Integer duration) {
        this.stationId = stationId;
        this.preStationId = preStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
