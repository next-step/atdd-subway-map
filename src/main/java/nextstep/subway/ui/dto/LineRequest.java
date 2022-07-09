package nextstep.subway.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

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
