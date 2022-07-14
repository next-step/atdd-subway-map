package nextstep.subway.applicaion.line.dto;

import lombok.Getter;
import nextstep.subway.domain.line.Line;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line toEntity() {
        return toEntity(null);
    }

    public Line toEntity(final Long id) {
        return Line.builder()
                .id(id)
                .name(this.name)
                .color(this.color)
                .upStationId(this.upStationId)
                .downStationId(this.downStationId)
                .distance(this.distance)
                .build();
    }
}
