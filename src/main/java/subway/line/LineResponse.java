package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.station.StationResponse;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "color"})
public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        List<StationResponse> stationResponses = Stream.of(line.getUpStation(), line.getDownStation())
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
