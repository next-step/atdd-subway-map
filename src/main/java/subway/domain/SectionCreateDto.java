package subway.domain;

public class SectionCreateDto {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionCreateDto(Long lineId, Long upStationId, Long downStationId, Long distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

}
