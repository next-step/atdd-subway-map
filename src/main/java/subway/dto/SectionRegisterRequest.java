package subway.dto;

public class SectionRegisterRequest {

    private String downStationId;
    private String upStationId;
    private Integer distance;

    public Long getDownStationId() {
        return Long.parseLong(downStationId);
    }

    public Long getUpStationId() {
        return Long.parseLong(upStationId);
    }

    public Integer getDistance() {
        return distance;
    }
}
