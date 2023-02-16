package subway.line.application.dto;

public class SectionAddRequest {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
