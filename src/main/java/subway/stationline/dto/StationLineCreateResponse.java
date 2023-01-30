package subway.stationline.dto;

import subway.station.Station;
import subway.stationline.StationLine;

import java.util.ArrayList;
import java.util.List;

public class StationLineCreateResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationLineStationCreateResponse> stations = new ArrayList<>();

    public StationLineCreateResponse() {
    }

    public StationLineCreateResponse(StationLine stationLine, List<Station> stations) {
        this.id = stationLine.getId();
        this.name = stationLine.getName();
        this.color = stationLine.getColor();

        for (Station station : stations) {
            this.stations.add(new StationLineStationCreateResponse(station.getId(), station.getName()));
        }
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

    public List<StationLineStationCreateResponse> getStations() {
        return stations;
    }
}
