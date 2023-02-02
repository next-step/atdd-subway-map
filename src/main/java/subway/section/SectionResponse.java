package subway.section;

public class SectionResponse {
    private long id;
    private long upStationId;
    private long downStationId;
    private long distance;

    public SectionResponse(long id, long upStationId, long downStationId, long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getDistance() {
        return distance;
    }
}
