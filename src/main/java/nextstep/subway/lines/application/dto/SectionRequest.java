package nextstep.subway.lines.application.dto;

public class SectionRequest {

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public SectionRequest() {

    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
