package subway.section;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(Long downStationId, Long upStationId, Integer distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
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

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "SectionRequest{" +
                "downStationId=" + downStationId +
                ", upStationId=" + upStationId +
                ", distance=" + distance +
                '}';
    }
}
