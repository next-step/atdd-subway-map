package nextstep.subway.applicaion.dto;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final StationResponses stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = new StationResponses(stations);
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

    public StationResponses getStations() {
        return stations;
    }
}
