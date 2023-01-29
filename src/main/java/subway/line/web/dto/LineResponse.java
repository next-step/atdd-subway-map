package subway.line.web.dto;

import lombok.Getter;
import subway.line.business.model.Line;
import subway.station.StationResponse;

import java.util.List;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Line line, List<StationResponse> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations;
    }

}
