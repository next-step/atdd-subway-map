package subway.section;

public class SectionRequest {

    private final String upStationId;
    private final String downStationId;
    private final Integer distance;

    public SectionRequest(String upStationId, String downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return Long.parseLong(upStationId);
    }

    public Long getDownStationId() {
        return Long.parseLong(downStationId);
    }

    public Integer getDistance() {
        return distance;
    }
}
