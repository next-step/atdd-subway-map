package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;

@Getter
@Setter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
