package subway.controller.dto;

import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {

    private final Long id;

    private final String name;

    private final String color;

    private final Set<StationResponse> stations;

    private final Long distance;

    public LineResponse(Long id, String name, String color, Set<Station> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = responseOf(stations);
        this.distance = distance;
    }

    private Set<StationResponse> responseOf(Set<Station> stations) {
        return stations.stream()
            .map(StationResponse::responseFrom)
            .collect(Collectors.toSet());
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getEndStations().getStations(),
            line.getDistance());
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

    public Set<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
