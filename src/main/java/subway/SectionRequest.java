package subway;

public class SectionRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;


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
