package nextstep.subway.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineCreateDto toCreateDto() {
        return LineCreateDto.builder()
                .name(this.name)
                .color(this.color)
                .upStationId(this.upStationId)
                .downStationId(this.downStationId)
                .distance(this.distance)
                .build();
    }

    public LineUpdateDto toUpdateDto() {
        return LineUpdateDto.builder()
                .name(this.name)
                .color(this.color)
                .build();
    }
}
