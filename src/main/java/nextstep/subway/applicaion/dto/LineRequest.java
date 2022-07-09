package nextstep.subway.applicaion.dto;

public class LineRequest {
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getDistance() {
        return distance;
    }
}
