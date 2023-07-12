package subway.dto;

import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = List.of(
            StationResponse.from(line.getUpStation()),
            StationResponse.from(line.getDownStation())
        );
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line);
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
