package subway;

public class LineRequest {
    private Long upStationId;
    private Long downStationId;
    private String name;

    public String getName() {
        return name;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }
}
