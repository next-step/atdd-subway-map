package subway.line.dto;

import subway.line.repository.Line;
import subway.station.dto.StationResponse;
import subway.station.repository.Station;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    private LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;

        stations.forEach(this::addStation);
    }

    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(line.getUpStation(), line.getDownStation())
        );
    }

    private void addStation(Station station) {
        stations.add(new StationResponse(station.getId(), station.getName()));
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
