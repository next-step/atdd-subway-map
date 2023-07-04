package subway.dto;

public class LineRequest {

    private Long id;


    private String color;
    private String name;

    private Long upStationId;
    private Long downStationId;

    private int distance;

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
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
