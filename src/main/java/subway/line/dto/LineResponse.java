package subway.line.dto;

import lombok.Builder;
import subway.line.Line;
import subway.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    @Builder
    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<Station> stations = line.getLineStations().stream().map(lineStation -> lineStation.getStation()).collect(Collectors.toList());
        return LineResponse.builder().id(line.getId()).name(line.getName()).color(line.getColor())
                .stations(stations)
                .build();
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

    public List<Station> getStations() {
        return stations;
    }
}
