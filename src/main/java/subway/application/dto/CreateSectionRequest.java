package subway.application.dto;

public class CreateSectionRequest {

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
