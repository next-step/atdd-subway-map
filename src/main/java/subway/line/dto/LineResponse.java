package subway.line.dto;


import subway.line.Line;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stationResponses;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
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

    public List<StationResponse> getStations() { return stations; }
}
