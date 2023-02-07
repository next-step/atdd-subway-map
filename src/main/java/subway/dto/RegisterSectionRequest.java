package subway.dto;

public class RegisterSectionRequest {

    private String downStationId;
    private String upStationId;
    private Long distance;

    public Long getDownStationId() {
        return Long.parseLong(downStationId);
    }

    public Long getUpStationId() {
        return Long.parseLong(upStationId);
    }

    public Long getDistance() {
        return distance;
    }
}
