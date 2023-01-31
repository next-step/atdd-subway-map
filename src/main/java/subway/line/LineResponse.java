package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.StationResponse;

@AllArgsConstructor
@Getter
public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse entityToResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStationList().stream()
                        .map(StationResponse::entityToResponse)
                        .collect(Collectors.toList()));
    }
}
