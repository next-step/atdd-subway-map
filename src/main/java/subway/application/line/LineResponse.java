package subway.application.line;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.application.station.StationResponse;

@AllArgsConstructor
@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

}
