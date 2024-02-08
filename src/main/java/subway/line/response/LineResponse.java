package subway.line.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.entity.Line;
import subway.station.entity.Station;
import subway.station.response.StationResponse;

import java.util.List;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder
    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
}
