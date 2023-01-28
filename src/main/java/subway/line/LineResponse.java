package subway.line;

import lombok.Builder;
import lombok.Getter;
import subway.StationResponse;

import java.util.List;
@Builder
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
}
