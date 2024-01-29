package subway.line;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Integer upStationId;
    private Integer downStationId;
    private Integer distance;
}
