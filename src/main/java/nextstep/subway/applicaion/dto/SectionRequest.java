package nextstep.subway.applicaion.dto;

public class SectionRequest {

    private String upStationId;

    private String downStationId;

    private Long distance;

    public Long getUpStationId() {
        return Long.parseLong(upStationId);
    }

    public Long getDownStationId() {
        return Long.parseLong(downStationId);
    }

    public Long getDistance() {
        return distance;
    }
}
