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

    public LineResponse(final Line savedLine) {
        this.id = savedLine.getId();
        this.name = savedLine.getName();
        this.color = savedLine.getColor();
        this.stations.add(createStationResponse(savedLine.getUpStation()));
        this.stations.add(createStationResponse(savedLine.getDownStation()));
    }

    public Long getId() {
        return id;
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
