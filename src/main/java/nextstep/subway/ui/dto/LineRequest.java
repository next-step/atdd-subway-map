package nextstep.subway.ui.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;

@Getter
@RequiredArgsConstructor
public class LineRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public LineCreateDto toDto() {
        return LineCreateDto.builder()
                .name(this.name)
                .color(this.color)
                .upStationId(this.upStationId)
                .downStationId(this.downStationId)
                .distance(this.distance)
                .build();
    }
}
