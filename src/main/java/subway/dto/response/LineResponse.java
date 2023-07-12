package subway.dto.response;

import lombok.Getter;
import subway.domain.Line;

import java.util.List;

@Getter
public class LineResponse {

    private Long id;

    private String color;

    private String name;

    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Line line) {
        this.id = line.getId();
        this.color = line.getColor();
        this.name = line.getName();
    }


    public LineResponse(Line line, List<StationResponse> stationResponses) {
        this.id = line.getId();
        this.color = line.getColor();
        this.name = line.getName();
        this.stations = stationResponses;
    }

}
