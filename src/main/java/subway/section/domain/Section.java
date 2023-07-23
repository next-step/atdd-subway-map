package subway.section.domain;

import subway.global.exception.BusinessException;

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

    public void checkIfDownStationAlreadyExisted(Long downStationId){
        if(this.downStationId.equals(downStationId) || this.upStationId.equals(downStationId))  throw new BusinessException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다");
    }

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
