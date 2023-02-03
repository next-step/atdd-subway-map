package subway.line;

import lombok.Getter;

@Getter
public class CreateLineRequest {
    private String name;

    private String color;

    private Long downStationId;
    private Long upStationId;

    private Integer distance;

    public Line toEntity() {
        return new Line(this.name, this.color, this.downStationId, this.upStationId, this.distance);
    }
}
