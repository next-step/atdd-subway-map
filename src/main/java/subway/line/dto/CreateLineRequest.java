package subway.line.dto;

import subway.line.Line;
import subway.line_station.LineStation;
import subway.station.Station;

public class CreateLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public CreateLineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity() {
        LineStation upLineStation = LineStation.builder()
                .station(Station.saveId(upStationId))
                .stationOrder(1L).nextDistance(distance)
                .build();
        LineStation downLineStation = LineStation.builder()
                .station(Station.saveId(downStationId))
                .stationOrder(2L).nextDistance(LineStation.lastDistance)
                .build();

        Line line = new Line(name, color);
        line.addLineStation(upLineStation);
        line.addLineStation(downLineStation);
        return line;
    }
}
