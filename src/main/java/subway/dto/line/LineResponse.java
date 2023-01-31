package subway.dto.line;

import subway.domain.Line;
import subway.dto.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int distance;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections()
                        .stations()
                        .stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toUnmodifiableList()),
                line.getDistance()
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

    public int getDistance() {
        return distance;
    }
}
