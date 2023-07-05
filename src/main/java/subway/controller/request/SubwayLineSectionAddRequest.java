package subway.controller.request;

public class SubwayLineSectionAddRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SubwayLineSectionAddRequest() {
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
