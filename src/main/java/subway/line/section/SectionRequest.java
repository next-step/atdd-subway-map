package subway.line.section;

public class SectionRequest {
    private long downStationId;
    private long upStationId;
    private int distance;

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }
}
