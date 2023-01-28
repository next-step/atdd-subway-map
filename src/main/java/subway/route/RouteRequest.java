package subway.route;

public class RouteRequest {
    private String name;
    private String color;
    private String upStationId;
    private String downStationId;
    private String distance;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getDistance() {
        return distance;
    }
}
