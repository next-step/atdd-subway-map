package subway.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineRequest {
    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

}
