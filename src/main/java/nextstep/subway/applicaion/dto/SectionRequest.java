package nextstep.subway.applicaion.dto;

public class SectionRequest {

    private String downStationId;

    private String upStationId;

    private Integer distance;

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
