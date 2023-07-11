package subway.line.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.domain.Line;
import subway.station.domain.Station;
import subway.station.dto.StationResponse;

public class LineResponse {

    private long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse() {
    }


    public LineResponse(long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(),
            line.getColor(), line.getStations().stream()
            .map(StationResponse::new)
            .collect(Collectors.toList()));
    }

    public long getId() {
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
