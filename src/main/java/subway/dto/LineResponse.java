package subway.dto;

import lombok.Getter;
import subway.domain.Station;

import java.util.List;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
}
