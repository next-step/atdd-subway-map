package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.List;


@Getter
@AllArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse toResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                List.of(StationResponse.toResponse(line.getUpStation()), StationResponse.toResponse(line.getDownStation()))
                );
    }
}
