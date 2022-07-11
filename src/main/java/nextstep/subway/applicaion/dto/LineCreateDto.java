package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.line.Line;

@Getter
@Builder
@RequiredArgsConstructor
public class LineCreateDto {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Line toDomain() {
        return Line.builder()
                .name(this.name)
                .color(this.color)
                .distance(this.distance)
                .upStationId(this.upStationId)
                .downStationId(this.downStationId)
                .build();
    }

}
