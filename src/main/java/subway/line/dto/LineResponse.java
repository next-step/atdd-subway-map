package subway.line.dto;

import subway.line.domain.Line;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                List.of(StationResponse.of(line.getUpStation()), StationResponse.of(line.getDownStation())));
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

    public List<Long> getStationIds() {
        return stations.stream().map(StationResponse::getId).collect(Collectors.toList());
    }
}
