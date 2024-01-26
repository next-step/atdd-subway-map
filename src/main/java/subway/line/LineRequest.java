package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LineRequest {

    private String name;
    private String color;
    private int distance;
    private Long upStationId;
    private Long downStationId;

}
