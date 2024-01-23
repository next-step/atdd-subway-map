package subway.line;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.StationResponse;

@AllArgsConstructor
@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

}
