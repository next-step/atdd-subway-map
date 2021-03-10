package nextstep.subway.line.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Section(Long upStationId, Long downStationId, int distance)
    {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void assignLine(Long lineId) {
        this.lineId = lineId;
    }

    public Long getUpStationId() { return upStationId;}

    public Long getDownStationId() { return downStationId; }

    public int getDistance() { return distance; }

    public Long getLineId() {return lineId;}
}
