package subway.line;

import java.util.List;
import lombok.Data;
import subway.station.StationResponse;

@Data
public class LineResponse {
    private final Long id;

    private final String name;

    private final String color;

    private final List<StationResponse> stations;
}
