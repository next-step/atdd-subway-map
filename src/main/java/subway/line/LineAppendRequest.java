package subway.line;

public class LineAppendRequest {
    private String upStationId;
    private String downStationId;

    private Long distance;

    public LineAppendRequest(String upStationId, String downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
