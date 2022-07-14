package nextstep.subway.applicaion.dto;

public class SectionRequest {

    private String upStationId;
    private String downStationId;
    private Integer distance;

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
