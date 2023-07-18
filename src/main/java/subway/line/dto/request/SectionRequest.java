package subway.line.dto.request;

public class SectionRequest {

    private final long downStationId;
    private final long upStationId;
    private final long distance;

    public SectionRequest(long downStationId, long upStationId, long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDistance() {
        return distance;
    }
}
