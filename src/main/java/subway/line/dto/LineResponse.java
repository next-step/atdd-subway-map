package subway.line.dto;

import java.util.List;
import subway.station.dto.StationResponse;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stationResponses;

    public LineResponse(Long id, String name, String color, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public Long getId() {
        return id;
    }
}
