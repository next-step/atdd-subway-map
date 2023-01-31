package subway.line.web.dto;

import lombok.Getter;
import subway.line.business.model.Line;
import subway.station.business.model.Station;

import java.util.List;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = line.getStations();
    }

}
