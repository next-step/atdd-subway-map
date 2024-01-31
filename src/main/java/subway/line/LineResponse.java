package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    protected LineResponse() {}

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations.add(
            createStationResponse(line.getUpStation())
        );
        this.stations.add(
            createStationResponse(line.getDownStation())
        );
    }

    private StationResponse createStationResponse(
        Station station
    ) {
        return new StationResponse(
            station.getId(),
            station.getName()
        );
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

    public List<StationResponse> getStations() {
        return stations;
    }
}