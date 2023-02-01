package subway.stationline.dto;

public class StationLineCreateRequest implements StationLineInterface {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public StationLineCreateRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String color() {
        return color;
    }

    @Override
    public Long upStationId() {
        return upStationId;
    }

    @Override
    public Long downStationId() {
        return downStationId;
    }

    @Override
    public Long distance() {
        return distance;
    }
}
