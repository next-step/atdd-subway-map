package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse of(Long id, String name, String color, List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        return new LineResponse(id, name, color, stationResponses);
    }
}
