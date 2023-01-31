package subway.line;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
