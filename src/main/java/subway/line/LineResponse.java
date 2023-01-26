package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.StationResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id", "name", "color"})
public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stationResponses = line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
