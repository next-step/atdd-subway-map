package subway.dto;

import java.util.List;

import subway.domain.Line;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Long distance;
    private final List<StationResponse> stations;

    public static LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(),
            List.of(StationResponse.createStationResponse(line.getUpStation()), StationResponse.createStationResponse(line.getDownStation())));
    }

    protected LineResponse(Long id, String name, String color, Long distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
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

    public Long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
