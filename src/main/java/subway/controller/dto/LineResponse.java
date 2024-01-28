package subway.controller.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static List<LineResponse> listOf(List<Line> lines, List<Station> stations) {
        return lines.stream()
                .map(line -> of(line, containStationWithLine(line, stations)))
                .collect(Collectors.toList());
    }

    public static LineResponse of(Line line, List<Station> stations) {
        List<Station> containStations = containStationWithLine(line, stations);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), StationResponse.listOf(containStations));
    }

    private static List<Station> containStationWithLine(Line line, List<Station> stations) {
        return line.stationIds().stream()
                .map(id -> stations.stream()
                        .filter(station -> station.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다.")))
                .collect(Collectors.toList());
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
