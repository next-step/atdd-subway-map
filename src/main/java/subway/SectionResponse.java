package subway;

public class SectionResponse {
    private final long id;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionResponse(Section section) {
        this.id = section.getId();
        this.upStationId = section.getUpStationId();
        this.downStationId = section.getDownStationId();
        this.distance = section.getDistance();
    }

    public long getId() {
        return id;
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
