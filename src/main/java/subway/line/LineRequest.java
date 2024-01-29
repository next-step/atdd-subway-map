package subway.line;

import lombok.Getter;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Integer upStationId;
    private Integer downStationId;
    private Integer distance;
}
