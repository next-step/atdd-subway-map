package subway;

import lombok.Getter;

@Getter
public class SubwayLineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

}
