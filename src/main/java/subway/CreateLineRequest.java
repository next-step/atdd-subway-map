package subway;

public class CreateLineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

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

    public Integer getDistance() {
        return distance;
    }

    public Line toEntity() {
        return new Line(
                this.name,
                this.color,
                this.upStationId,
                this.downStationId,
                this.distance
        );
    }
}
