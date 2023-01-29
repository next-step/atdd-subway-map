package subway.line.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Line {

    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

}
