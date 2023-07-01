package subway.line;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import subway.station.StationResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineCreateResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineCreateResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = List.of(new StationResponse(line.getUpStation()), new StationResponse(line.getDownStation()));
    }
}
