package subway.controller.request;

import lombok.Getter;
import subway.common.Comment;
import subway.repository.entity.Line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class LineRequest {

    @Comment("노선 이름")
    @NotBlank
    private String name;

    @Comment("노선 색")
    @NotBlank
    private String color;

    @Comment("노선 시작역")
    @NotNull
    private Long upStationId;

    @Comment("노선 종료역")
    @NotNull
    private Long downStationId;

    @Comment("노선 거리")
    @NotNull
    private Integer distance;

    public Line toEntity() {
        return Line.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
