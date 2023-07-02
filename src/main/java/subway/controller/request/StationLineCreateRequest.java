package subway.controller.request;


public class StationLineCreateRequest {

    private String lineName;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;


    public String getLineName() {
        return lineName;
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
