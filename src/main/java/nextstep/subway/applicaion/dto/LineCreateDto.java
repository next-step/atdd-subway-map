package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;

@Getter
@Builder
@RequiredArgsConstructor
public class LineCreateDto {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public Line toDomain() {
        return Line.builder()
                .name(this.name)
                .color(this.color)
                .distance(this.distance)
                .build();
    }

}
