package subway.stationline.dto;

import subway.station.Station;
import subway.stationline.StationLine;

import java.util.ArrayList;
import java.util.List;

public class StationLineReadListResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationLineStationReadListResponse> stations = new ArrayList<>();
    private Long upStationId;
    private Long downStationId;

    public StationLineReadListResponse() {
    }

    public StationLineReadListResponse(StationLine stationLine, List<Station> stations) {
        this.id = stationLine.getId();
        this.name = stationLine.getName();
        this.color = stationLine.getColor();

        for (Station station : stations) {
            this.stations.add(new StationLineStationReadListResponse(station.getId(), station.getName()));
        }

        this.upStationId = stationLine.getUpStationId();
        this.downStationId = stationLine.getDownStationId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationLineStationReadListResponse> getStations() {
        return stations;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
