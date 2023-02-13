package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.Station;
import subway.station.StationResponse;

@Getter
@AllArgsConstructor
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private StationResponse upStation;
    private StationResponse downStation;

    public static LineResponse of(Long id, String name, String color, List<Station> stations,
        Station upStation, Station downStation) {
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of)
            .collect(Collectors.toList());
        return new LineResponse(id, name, color, stationResponses, StationResponse.of(upStation),
            StationResponse.of(downStation));
    }
}
