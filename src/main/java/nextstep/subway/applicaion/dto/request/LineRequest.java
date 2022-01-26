package nextstep.subway.applicaion.dto.request;

public class LineRequest {

    private String color;
    private String name;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest(
            String color,
            String name,
            Long upStationId,
            Long downStationId,
            int distance
    ) {
        this.color = color;
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
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

    public int getDistance() {
        return distance;
    }
}
