package subway.dto;

public class LineRequest {

    private String name;
    private String color;
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
