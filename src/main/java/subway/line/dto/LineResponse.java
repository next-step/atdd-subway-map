package subway.line.dto;

import subway.line.domain.Line;
import subway.station.domain.Station;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Long> stationIds;

    public LineResponse(Long id, String name, String color, List<Long> stationIds) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationIds = stationIds;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                line.getStationIds());
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

    public List<Long> getStationIds() {
        return stationIds;
    }
}
