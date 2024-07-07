package subway;

public class LineCreateRequest {

    private String lineName;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineCreateRequest(String lineName, String color, Long upStationId, Long downStationId, Long distance) {
        this.lineName = lineName;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
