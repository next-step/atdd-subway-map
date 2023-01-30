package subway.line;

import subway.*;

import java.util.*;
import java.util.stream.*;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getLineStations().getValues()
                        .stream()
                        .map(LineResponse::getStationResponse)
                        .collect(Collectors.toList())
        );
    }

    private static StationResponse getStationResponse(LineStation lineStation) {
        return new StationResponse(lineStation.getStation().getId(), lineStation.getStation().getName());
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
