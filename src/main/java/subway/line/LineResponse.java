package subway.line;

public class LineResponse {

    private Long id;

    private String name;
    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public LineResponse(Long id, String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {return id;}

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

    public Long getDistance() {
        return distance;
    }
}
