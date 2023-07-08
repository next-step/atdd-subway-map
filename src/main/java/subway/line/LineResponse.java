package subway.line;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import subway.station.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private Long distance;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
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

    public static List<LineResponse> of(List<Line> lines) {
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public static LineResponse of(Line line) {
        StationResponse upStation = StationResponse.of(line.getUpStation());
        StationResponse downStation = StationResponse.of(line.getDownStation());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation), line.getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse response = (LineResponse) o;
        return Objects.equals(id, response.id) && Objects.equals(name, response.name) && Objects.equals(color, response.color) && Objects.equals(stations, response.stations) && Objects.equals(distance, response.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations, distance);
    }
}
