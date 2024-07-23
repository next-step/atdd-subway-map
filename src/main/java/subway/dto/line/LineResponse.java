package subway.dto.line;

import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations ) {
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

    public static LineResponse createResponse(Line line, List<Station> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream()
                        .map(StationResponse::createResponse)
                        .collect(Collectors.toList())
        );
    }
}
