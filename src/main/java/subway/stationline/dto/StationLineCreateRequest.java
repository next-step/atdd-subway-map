package subway.stationline.dto;

import subway.stationline.StationLine;

public class StationLineCreateRequest {
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

    public StationLine convertToEntity() {
        return new StationLine(name, color, upStationId, downStationId, distance);
    }


}
