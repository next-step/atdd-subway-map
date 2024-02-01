package subway.controller.dto;

public class SectionCreateRequest {

    private String downStationId;
    private String upStationId;
    private int distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String downStationId, String upStationId, int distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }
}
