package nextstep.subway.applicaion.dto;

public class SectionAddRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }
}
