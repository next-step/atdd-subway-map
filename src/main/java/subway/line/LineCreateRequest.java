package subway.line;

import lombok.Getter;

@Getter
public class LineCreateRequest {
    private String name;
    private String color;
    private Integer upStationId;
    private Integer downStationId;
    private Integer distance;

    public Line to() {
        return Line.builder()
                .name(this.getName())
                .color(this.getColor())
                .upStationId(this.getUpStationId())
                .downStationId(this.getDownStationId())
                .distance(this.getDistance())
                .build();
    }
}
