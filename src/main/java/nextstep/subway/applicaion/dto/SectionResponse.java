package nextstep.subway.applicaion.dto;

public class SectionResponse {
    private String upStationId;
    private String downStationId;
    private Integer distance;

    public SectionResponse(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = String.valueOf(upStationId);
        this.downStationId = String.valueOf(downStationId);
        this.distance = distance;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
