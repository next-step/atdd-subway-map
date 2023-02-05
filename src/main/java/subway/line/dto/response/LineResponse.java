package subway.line.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
}
