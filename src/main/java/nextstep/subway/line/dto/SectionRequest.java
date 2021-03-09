package nextstep.subway.line.dto;

public class SectionRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;

    public SectionRequest(){}

    public SectionRequest(Long upStationId, Long downStationId,  int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

}
