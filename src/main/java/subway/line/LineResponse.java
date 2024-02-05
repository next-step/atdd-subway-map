package subway.line;

import subway.station.StationResponse;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.stations = List.of(new StationResponse(line.getDownStation()), new StationResponse(line.getDownStation()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
