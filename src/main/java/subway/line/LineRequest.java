package subway.line;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
}
