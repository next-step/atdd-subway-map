package subway.line.service.dto;

import subway.line.repository.domain.Line;
import subway.station.service.dto.StationResponse;

import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    private LineResponse(final Long id, final String name, final String color, final int distance, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(
                line.getId()
                , line.getName()
                , line.getColor()
                , line.getDistance()
                , List.of(StationResponse.from(line.getUpStation()), StationResponse.from(line.getDownStation()))
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
