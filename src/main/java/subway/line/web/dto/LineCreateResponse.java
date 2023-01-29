package subway.line.web.dto;

import lombok.Getter;
import subway.line.business.model.Line;
import subway.station.StationResponse;

import java.util.List;

@Getter
public class LineCreateResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineCreateResponse(Line line, List<StationResponse> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations;
    }

}
