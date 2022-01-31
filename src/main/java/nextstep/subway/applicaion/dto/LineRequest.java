package nextstep.subway.applicaion.dto;

public class LineRequest {
    // TODO: 아무래도 Not Null 옵션을 사용하는게 나을듯?
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

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
