package subway.line.section;

public class SectionResponse {
    private long id;
    private long upStationId;
    private long downStationId;
    private int distance;

    public SectionResponse(long id, long upStationId, long downStationId, int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public long getId() {
        return id;
    }
}
