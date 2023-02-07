package subway.section;

public class SectionCreateRequest {
    private long downStationId;
    private long upStationId;
    private long distance;

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDistance() {
        return distance;
    }

    public SectionCreateRequest(long downStationId, long upStationId, long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }
}
