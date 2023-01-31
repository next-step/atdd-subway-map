package subway;

import lombok.Getter;

@Getter
public class SubwayLineRequest {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;


    public SubwayLineRequest(String name, String color, Long upStationId, Long downStationId,
        Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
