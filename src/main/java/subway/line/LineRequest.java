package subway.line;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineRequest {
    private String name;
    private String color;
    private long downStationId;
    private long upStationId;
    private int distance;

    public static LineRequest of(String name, String color, long downStationId, long upStationId, int distance) {
        return LineRequest.builder()
            .name(name)
            .color(color)
            .downStationId(downStationId)
            .upStationId(upStationId)
            .distance(distance)
            .build();
    }
}
