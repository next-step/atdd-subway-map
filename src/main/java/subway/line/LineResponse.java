package subway.line;

import lombok.Value;
import subway.station.StationResponse;

import java.util.List;

@Value
class LineResponse {
    Long id;
    String name;
    String color;
    List<StationResponse> stations;
}
