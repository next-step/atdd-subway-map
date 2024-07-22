package subway.line;

public class LineRequest {
    private String name;
    private String color;
    private int upStationId;
    private int downStationId;
    private int distance;

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }
}
