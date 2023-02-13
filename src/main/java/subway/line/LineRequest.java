package subway.line;

public class LineRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;


    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
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

    public Long getDistance() {
        return distance;
    }

}
