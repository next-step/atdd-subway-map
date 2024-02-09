package subway.section;

public class SectionRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Section createSection(int currentLineDistance) {
        return new Section(upStationId, downStationId, distance-currentLineDistance);
    }
}
