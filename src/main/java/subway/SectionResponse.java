package subway;

public class SectionResponse {
    private long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse(Section section) {
        if (section != null) {
            this.id = section.getId();
            this.upStationId = section.getUpStationId();
            this.downStationId = section.getDownStationId();
            this.distance = section.getDistance();
        }
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
