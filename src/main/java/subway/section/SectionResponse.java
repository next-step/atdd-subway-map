package subway.section;

public class SectionResponse {
    private Long sectionId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse(Long sectionId, Long upStationId, Long downStationId, int distance) {
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
