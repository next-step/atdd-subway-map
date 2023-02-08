package subway.line;

import lombok.*;

@Getter
public class LineCreateRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

}
