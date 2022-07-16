package nextstep.subway.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "color is required")
    private String color;

    @NotNull(message = "upStationId is required")
    private Long upStationId;

    @NotNull(message = "downStationId is required")
    private Long downStationId;

    @NotNull(message = "distance is required")
    private Integer distance;

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
