package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;


}
