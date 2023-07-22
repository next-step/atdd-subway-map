package subway.section.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long downStationId;
    private Long upStationId;
    private Integer distance;
    private Long lineId;

    public Section(Long downStationId, Long upStationId, Integer distance, Long lineId) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    protected Section() {}

    public Long getId() {
        return id;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
