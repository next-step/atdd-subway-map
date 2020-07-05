package nextstep.subway.line.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity

public class LineStation{

    @Id @GeneratedValue
    private Long id;
    private Long preStationId;
    private Long stationId;
    private Integer distance;
    private Integer duration;

    protected LineStation() {
    }

    public LineStation(Long preStationId, Long stationId, Integer distance, Integer duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

}
